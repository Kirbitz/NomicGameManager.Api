package nomic.domain.entities

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LoginNameTest {

    @Test
    fun test_loginName_tooLong() {
        Assertions.assertThat(LoginName.canParse("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")).isFalse
        Assertions.assertThat(LoginName.canParse("OneRingToRuleThemAll_OneRingToFindThem_OneRingToBringThemAll_AndInTheDarknessBindThem")).isFalse
    }

    @Test
    fun test_loginName_tooShort() {
        Assertions.assertThat(LoginName.canParse("")).isFalse
    }

    @Test
    fun test_loginName_minLength() {
        val name = "a"
        Assertions.assertThat(LoginName(name).rawName).isEqualTo(name)
    }

    @Test
    fun test_loginName_maxLength() {
        val name = "abcdefghjiklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRS"
        Assertions.assertThat(LoginName(name).rawName).isEqualTo(name)
    }

    @Test
    fun test_loginName_throwsException_if_canParse_false() {
        val name = "Hello, World!"
        Assertions.assertThat(LoginName.canParse(name)).isFalse
        Assertions.assertThatThrownBy {
            LoginName(name)
        }.isInstanceOf(InvalidLoginNameException::class.java)
    }

    @Test
    fun test_loginName_canParse_invalidCharacters() {
        Assertions.assertThat(LoginName.canParse("x=(-b+-@(b^2-4ac))/2a")).isFalse
        Assertions.assertThat(LoginName.canParse("E = mc^2")).isFalse
        Assertions.assertThat(LoginName.canParse("Why hello there!")).isFalse
        Assertions.assertThat(LoginName.canParse("I forget what text like Z̷͙̗̻͖̣̹͉̫̬̪̖̤͆ͤ̓ͫͭ̀̐͜͞ͅͅαлγo is")).isFalse
        Assertions.assertThat(LoginName.canParse("Hello\nThere")).isFalse
    }

    @Test
    fun test_loginName_validCharacters() {
        val user1 = LoginName("ToBe0rN0tT0B3")
        val user2 = LoginName("TolkienFan17")
        val user3 = LoginName("__SomeUsername__")
        val user4 = LoginName("the-apple-tree")

        Assertions.assertThat(user1.rawName).isEqualTo("ToBe0rN0tT0B3")
        Assertions.assertThat(user2.rawName).isEqualTo("TolkienFan17")
        Assertions.assertThat(user3.rawName).isEqualTo("__SomeUsername__")
        Assertions.assertThat(user4.rawName).isEqualTo("the-apple-tree")
    }

    @Test
    fun test_loginName_canParse_null_fails() {
        Assertions.assertThat(LoginName.canParse(null)).isFalse
    }
}
