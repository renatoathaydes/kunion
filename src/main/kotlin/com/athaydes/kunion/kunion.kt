package com.athaydes.kunion

/**
 * A type union between 2 or more types.
 */
sealed class Union
{
    /**
     * An instance of an union type.
     */
    interface Instance<out V>
    {

        /**
         * The value of a variable whose type is a union type.
         *
         * Notice that the instance itself always has only one type, V.
         */
        val value: V
    }

    /**
     * Rotate the types in this union.
     *
     * The rotation is performed from left to right, so that the last type becomes the first,
     * the first type becomes the second and so on.
     */
    abstract fun rotate(): Union

    /**
     * Convert this Union to a [U2] instance.
     *
     * * [U2] instances return themselves.
     * * [U3] instances of the form U3<A, B, C> return {@code U2<A, U2<B, C>}
     * * [U4] instances of the form U4<A, B, C, D> return {@code U2<A, U3<B, C, D>}
     */
    abstract fun asU2(): Union

    /**
     * Union of 2 types.
     */
    sealed class U2<out A, out B> : Union()
    {
        override fun rotate(): U2<B, A> = when (this)
        {
            is U2_1<A> -> U2_2(this.value)
            is U2_2<B> -> U2_1(this.value)
        }

        abstract override fun asU2(): U2<A, B>

        data class U2_1<out A>(override val value: A) : U2<A, Nothing>(), Instance<A>
        {
            override fun asU2(): U2<A, Nothing> = this
        }

        data class U2_2<out B>(override val value: B) : U2<Nothing, B>(), Instance<B>
        {
            override fun asU2(): U2<Nothing, B> = this
        }
    }

    /**
     * Union of 3 types.
     */
    sealed class U3<out A, out B, out C> : Union()
    {
        override fun rotate(): U3<C, A, B> = when (this)
        {
            is U3_1<A> -> U3_2(this.value)
            is U3_2<B> -> U3_3(this.value)
            is U3_3<C> -> U3_1(this.value)
        }

        abstract override fun asU2(): U2<A, U2<B, C>>

        data class U3_1<out A>(override val value: A) : U3<A, Nothing, Nothing>(), Instance<A>
        {
            override fun asU2(): U2<A, Nothing> = U2.U2_1(value)
        }

        data class U3_2<out B>(override val value: B) : U3<Nothing, B, Nothing>(), Instance<B>
        {
            override fun asU2(): U2<Nothing, U2<B, Nothing>> = U2.U2_2(U2.U2_1(value))
        }

        data class U3_3<out C>(override val value: C) : U3<Nothing, Nothing, C>(), Instance<C>
        {
            override fun asU2(): U2<Nothing, U2<Nothing, C>> = U2.U2_2(U2.U2_2(value))
        }
    }

    /**
     * Union of 4 types.
     */
    sealed class U4<out A, out B, out C, out D> : Union()
    {
        override fun rotate(): U4<D, A, B, C> = when (this)
        {
            is U4_1<A> -> U4_2(this.value)
            is U4_2<B> -> U4_3(this.value)
            is U4_3<C> -> U4_4(this.value)
            is U4_4<D> -> U4_1(this.value)
        }

        abstract override fun asU2(): U2<A, U3<B, C, D>>

        data class U4_1<out A>(override val value: A) : U4<A, Nothing, Nothing, Nothing>(), Instance<A>
        {
            override fun asU2(): U2<A, Nothing> = U2.U2_1(value)
        }

        data class U4_2<out B>(override val value: B) : U4<Nothing, B, Nothing, Nothing>(), Instance<B>
        {
            override fun asU2(): U2<Nothing, U3<B, Nothing, Nothing>> = U2.U2_2(U3.U3_1(value))
        }

        data class U4_3<out C>(override val value: C) : U4<Nothing, Nothing, C, Nothing>(), Instance<C>
        {
            override fun asU2(): U2<Nothing, U3<Nothing, C, Nothing>> = U2.U2_2(U3.U3_2(value))
        }

        data class U4_4<out D>(override val value: D) : U4<Nothing, Nothing, Nothing, D>(), Instance<D>
        {
            override fun asU2(): U2<Nothing, U3<Nothing, Nothing, D>> = U2.U2_2(U3.U3_3(value))
        }
    }

}
