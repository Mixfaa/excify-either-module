package com.mixfa.excify

import arrow.core.Either

class UnresolvedValueException(val value: Any) : FastThrowable(value.toString())

/**
 * Arrow`s Either
 */
typealias EitherError<T> = Either<Throwable, T>

fun <T> EitherError<T>.orThrow(): T = when (this) {
    is Either.Left -> throw this.value
    is Either.Right -> this.value
}

/**
 * excify scopes
 */
object Excify {
    inline fun <reified TargetType> catch(block: () -> TargetType): EitherError<TargetType> = Either.catch(block)

    inline fun <reified TargetType> rethrow(block: () -> Any): TargetType = when (val returnedValue = block()) {
        is TargetType -> returnedValue
        is FastThrowable -> throw returnedValue
        else -> throw UnresolvedValueException(returnedValue)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified TargetType> wrapAndCatch(block: () -> Any): EitherError<TargetType> =
        when (val returnedValue = Either.catch(block)) {
            is Either.Left -> returnedValue
            is Either.Right -> {
                when (val value = returnedValue.value) {
                    is TargetType -> returnedValue as Either.Right<TargetType> //Either.Right(value)
                    is Throwable -> Either.Left(value)
                    else -> Either.Left(UnresolvedValueException(value))
                }
            }
        }

    inline fun <reified TargetType> wrap(block: () -> Any): EitherError<TargetType> =
        when (val returnedValue = block()) {
            is TargetType -> Either.Right(returnedValue)
            is Throwable -> Either.Left(returnedValue)
            else -> Either.Left(UnresolvedValueException(returnedValue))
        }
}
