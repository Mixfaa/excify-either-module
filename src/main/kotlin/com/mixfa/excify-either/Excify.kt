package com.mixfa.`excify-either`

import arrow.core.Either
import arrow.core.memoize
import com.mixfa.excify.FastException

class UnresolvedValueException(val value: Any) : FastException(value.toString())

/**
 * Arrow`s Either
 */
typealias EitherError<T> = Either<Throwable, T>

fun <T> EitherError<T>.orThrow(): T = when (this) {
    is Either.Left -> throw this.value
    is Either.Right -> this.value
}

private val memorizedConstructor = ::FastException.memoize()

/**
 * Makes instance of FastException, for same args returns same instance
 * @see com.mixfa.excify.FastException
 * @see arrow.core.memoize
 */
public fun makeMemorizedException(message: String) = memorizedConstructor(message)

/**
 * excify scopes
 */
object Excify {
    inline fun <reified TargetType> catch(block: () -> TargetType): EitherError<TargetType> = Either.catch(block)

    inline fun <reified TargetType> rethrow(block: () -> Any): TargetType = when (val returnedValue = block()) {
        is TargetType -> returnedValue
        is FastException -> throw returnedValue
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
