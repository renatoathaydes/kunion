import com.athaydes.kunion.Union
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

typealias UString = Union.U2.U2_1<String>
typealias UBoolean = Union.U2.U2_2<Boolean>

typealias U3String = Union.U3.U3_1<String>
typealias U3Char = Union.U3.U3_2<Char>
typealias U3Int = Union.U3.U3_3<Int>

typealias U4String = Union.U4.U4_1<String>
typealias U4Char = Union.U4.U4_2<Char>
typealias U4Int = Union.U4.U4_3<Int>
typealias U4Bool = Union.U4.U4_4<Boolean>

class KunionTestSuite
{

    @Test
    fun u2()
    {

        val string: Union.U2<String, Boolean> = Union.U2.U2_1("hello")

        val text: String = when (string)
        {
            is UString  -> string.value
            is UBoolean -> "True? " + (string.value == false)
        }

        assertThat(text, equalTo("hello"))

        val bool: Union.U2<String, Boolean> = Union.U2.U2_2(true)

        val flag: Int = when (bool)
        {
            is UString  -> 1
            is UBoolean -> 2
        }

        assertThat(flag, `is`(2))
    }

    @Test
    fun u2rotate()
    {
        val s1: Union.U2<String, Boolean> = Union.U2.U2_1("hello")

        val s2: Union.U2<Boolean, String> = s1.rotate()

        assert(s2 is Union.U2.U2_2<String>)
        s2 as Union.U2.U2_2<String>
        assertThat(s2.value, equalTo("hello"))
    }

    @Test
    fun U2AsU2()
    {
        val a: Union.U2<Int, Float> = Union.U2.U2_1(43)
        assertThat(a, equalTo(a.asU2()))

        val b: Union.U2<Int, Float> = Union.U2.U2_2(0.34F)
        assertThat(b, equalTo(b.asU2()))
    }

    @Test
    fun U2Use()
    {
        val a: Union.U2<Int, Float> = Union.U2.ofA(43)
        val result1 = a.use({ a -> "Got Int $a" }, { b -> "Got Float $b" })

        assertThat(result1, equalTo("Got Int 43"))

        val b: Union.U2<Int, Float> = Union.U2.ofB(0.34F)
        val result2 = b.use({ a -> "Got Int $a" }, { b -> "Got Float $b" })

        assertThat(result2, equalTo("Got Float 0.34"))
    }

    @Test
    fun U2Map()
    {
        val a: Union.U2<Int, Float> = Union.U2.U2_1(43)
        val result1 = a.map({ a -> a - 1 }, { b -> "Got Float $b" })

        assertThat<Union.U2<Int, String>>(result1, equalTo(Union.U2.U2_1(42)))

        val b: Union.U2<Int, Float> = Union.U2.U2_2(0.34F)
        val result2 = b.map({ a -> "Got Int $a" }, { b -> b + 0.1F })

        assertThat<Union.U2<String, Float>>(result2, equalTo(Union.U2.U2_2(0.44F)))
    }

    @Test
    fun u3()
    {

        val string: Union.U3<String, Char, Int> = Union.U3.U3_1("hello")

        val text: String = when (string)
        {
            is U3String -> string.value
            is U3Char   -> "? " + (string.value == 'a')
            is U3Int    -> "True? " + (string.value == 33)
        }

        assertThat(text, equalTo("hello"))

        val int: Union.U3<String, Char, Int> = Union.U3.U3_3(42)

        val flag: Int = when (int)
        {
            is U3String -> 1
            is U3Char   -> 2
            is U3Int    -> 3
        }

        assertThat(flag, `is`(3))
    }

    @Test
    fun U3AsU2()
    {
        val value1: Union.U3<String, Char, Int> = Union.U3.U3_1("hi")
        val value1AsU2: Union.U2<String, Union.U2<Char, Int>> = Union.U2.U2_1("hi")

        assertThat(value1.asU2(), equalTo(value1AsU2))

        val value2: Union.U3<String, Char, Int> = Union.U3.U3_2('z')
        val value2AsU2: Union.U2<String, Union.U2<Char, Int>> = Union.U2.U2_2(Union.U2.U2_1('z'))

        assertThat(value2.asU2(), equalTo(value2AsU2))

        val value3: Union.U3<String, Char, Int> = Union.U3.U3_3(3)
        val value3AsU2: Union.U2<String, Union.U2<Char, Int>> = Union.U2.U2_2(Union.U2.U2_2(3))

        assertThat(value3.asU2(), equalTo(value3AsU2))
    }

    @Test
    fun u3rotate()
    {
        val s1: Union.U3<String, Char, Int> = Union.U3.U3_1("hello")

        val s2: Union.U3<Int, String, Char> = s1.rotate()

        assert(s2 is Union.U3.U3_2<String>)
        s2 as Union.U3.U3_2<String>
        assertThat(s2.value, equalTo("hello"))
    }

    @Test
    fun U3Use()
    {
        val a: Union.U3<Int, Float, Boolean> = Union.U3.ofA(43)
        val result1 = a.use({ a -> "Got Int $a" }, { b -> "Got Float $b" }, { c -> "Got Boolean $c" })

        assertThat(result1, equalTo("Got Int 43"))

        val b: Union.U3<Int, Float, Boolean> = Union.U3.ofB(0.34F)
        val result2 = b.use({ a -> "Got Int $a" }, { b -> "Got Float $b" }, { c -> "Got Boolean $c" })

        assertThat(result2, equalTo("Got Float 0.34"))

        val c: Union.U3<Int, Float, Boolean> = Union.U3.ofC(true)
        val result3 = c.use({ a -> "Got Int $a" }, { b -> "Got Float $b" }, { c -> "Got Boolean $c" })

        assertThat(result3, equalTo("Got Boolean true"))
    }

    @Test
    fun U3Map()
    {
        val a: Union.U3<Int, Float, Boolean> = Union.U3.U3_1(43)
        val result1 = a.map({ a -> a - 1 }, { b -> "Got Float $b" }, { c -> if (c) 'c' else 'x' })

        assertThat<Union.U3<Int, String, Char>>(result1, equalTo(Union.U3.U3_1(42)))

        val b: Union.U3<Int, Float, Boolean> = Union.U3.U3_2(0.34F)
        val result2 = b.map({ a -> "Got Int $a" }, { b -> b + 0.1F }, { c -> if (c) 'c' else 'x' })

        assertThat<Union.U3<String, Float, Char>>(result2, equalTo(Union.U3.U3_2(0.44F)))

        val c: Union.U3<Int, Float, Boolean> = Union.U3.U3_3(true)
        val result3 = c.map({ a -> "Got Int $a" }, { b -> b + 0.1F }, { c -> if (c) 'c' else 'x' })

        assertThat<Union.U3<String, Float, Char>>(result3, equalTo(Union.U3.U3_3('c')))
    }

    @Test
    fun u4()
    {

        val string: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_1("hello")

        val text: String = when (string)
        {
            is U4String -> string.value
            is U4Char   -> "? " + (string.value == 'a')
            is U4Int    -> "True? " + (string.value == 33)
            is U4Bool   -> "True? " + (string.value == true)
        }

        assertThat(text, equalTo("hello"))

        val int: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_3(42)

        val flag: Int = when (int)
        {
            is U4String -> 1
            is U4Char   -> 2
            is U4Int    -> 3
            is U4Bool   -> 4
        }

        assertThat(flag, `is`(3))
    }

    @Test
    fun u4rotate()
    {
        val s1: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_1("hello")

        val s2: Union.U4<Boolean, String, Char, Int> = s1.rotate()

        assert(s2 is Union.U4.U4_2<String>)
        s2 as Union.U4.U4_2<String>
        assertThat(s2.value, equalTo("hello"))
    }

    @Test
    fun U4AsU2()
    {
        val value1: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_1("hi")
        val value1AsU2: Union.U2<String, Union.U3<Char, Int, Boolean>> = Union.U2.U2_1("hi")

        assertThat(value1.asU2(), equalTo(value1AsU2))

        val value2: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_2('z')
        val value2AsU2: Union.U2<String, Union.U3<Char, Int, Boolean>> = Union.U2.U2_2(Union.U3.U3_1('z'))

        assertThat(value2.asU2(), equalTo(value2AsU2))

        val value3: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_3(3)
        val value3AsU2: Union.U2<String, Union.U3<Char, Int, Boolean>> = Union.U2.U2_2(Union.U3.U3_2(3))

        assertThat(value3.asU2(), equalTo(value3AsU2))

        val value4: Union.U4<String, Char, Int, Boolean> = Union.U4.U4_4(false)
        val value4AsU2: Union.U2<String, Union.U3<Char, Int, Boolean>> = Union.U2.U2_2(Union.U3.U3_3(false))

        assertThat(value4.asU2(), equalTo(value4AsU2))
    }

    @Test
    fun U4Use()
    {
        val a: Union.U4<Int, Float, Boolean, Char> = Union.U4.ofA(43)
        val result1 = a.use(
                { a -> "Got Int $a" }, { b -> "Got Float $b" },
                { c -> "Got Boolean $c" }, { d -> "Got Char $d" })

        assertThat(result1, equalTo("Got Int 43"))

        val b: Union.U4<Int, Float, Boolean, Char> = Union.U4.ofB(0.34F)
        val result2 = b.use(
                { a -> "Got Int $a" }, { b -> "Got Float $b" },
                { c -> "Got Boolean $c" }, { d -> "Got Char $d" })

        assertThat(result2, equalTo("Got Float 0.34"))

        val c: Union.U4<Int, Float, Boolean, Char> = Union.U4.ofC(true)
        val result3 = c.use(
                { a -> "Got Int $a" }, { b -> "Got Float $b" },
                { c -> "Got Boolean $c" }, { d -> "Got Char $d" })

        assertThat(result3, equalTo("Got Boolean true"))

        val d: Union.U4<Int, Float, Boolean, Char> = Union.U4.ofD('x')
        val result4 = d.use(
                { a -> "Got Int $a" }, { b -> "Got Float $b" },
                { c -> "Got Boolean $c" }, { d -> "Got Char $d" })

        assertThat(result4, equalTo("Got Char x"))
    }

    @Test
    fun U4Map()
    {
        val a: Union.U4<Int, Float, Boolean, Char> = Union.U4.U4_1(43)
        val result1 = a.map(
                { a -> a - 1 }, { b -> "Got Float $b" },
                { c -> if (c) 'c' else 'x' }, { d -> d == 'z' })

        assertThat<Union.U4<Int, String, Char, Boolean>>(result1, equalTo(Union.U4.U4_1(42)))

        val b: Union.U4<Int, Float, Boolean, Char> = Union.U4.U4_2(0.34F)
        val result2 = b.map(
                { a -> "Got Int $a" }, { b -> b + 0.1F },
                { c -> if (c) 'c' else 'x' }, { d -> d == 'z' })

        assertThat<Union.U4<String, Float, Char, Boolean>>(result2, equalTo(Union.U4.U4_2(0.44F)))

        val c: Union.U4<Int, Float, Boolean, Char> = Union.U4.U4_3(true)
        val result3 = c.map(
                { a -> "Got Int $a" }, { b -> b + 0.1F },
                { c -> if (c) 'c' else 'x' }, { d -> d == 'z' })

        assertThat<Union.U4<String, Float, Char, Boolean>>(result3, equalTo(Union.U4.U4_3('c')))

        val d: Union.U4<Int, Float, Boolean, Char> = Union.U4.U4_4('m')
        val result4 = d.map(
                { a -> "Got Int $a" }, { b -> b + 0.1F },
                { c -> if (c) 'c' else 'x' }, { d -> d == 'z' })

        assertThat<Union.U4<String, Float, Char, Boolean>>(result4, equalTo(Union.U4.U4_4(false)))
    }

}