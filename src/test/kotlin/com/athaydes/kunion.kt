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

}