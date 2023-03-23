package nomic.domain.entities

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LoginNameTest {

    @Test
    fun test_loginName_tooLong() {
        Assertions.assertThatThrownBy { LoginName("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ") }
        Assertions.assertThatThrownBy { LoginName("OneRingToRuleThemAll_OneRingToFindThem_OneRingToBringThemAll_AndInTheDarknessBindThem") }
    }

    @Test
    fun test_loginName_tooShort() {
        Assertions.assertThatThrownBy { LoginName("") }
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
    fun test_loginName_invalidCharacters() {
        Assertions.assertThatThrownBy { LoginName("x=(-b+-@(b^2-4ac))/2a") }
        Assertions.assertThatThrownBy { LoginName("E = mc^2") }
        Assertions.assertThatThrownBy { LoginName("Why hello there!") }
        Assertions.assertThatThrownBy { LoginName("I forget what text like Z̷͙̗̻͖̣̹͉̫̬̪̖̤͆ͤ̓ͫͭ̀̐͜͞ͅͅαлγo is") }
        Assertions.assertThatThrownBy { LoginName("Hello\nThere") }
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
}
