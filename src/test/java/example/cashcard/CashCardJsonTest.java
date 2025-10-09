package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Test
    public void cashCardSerializationTest() throws Exception {
        final CashCard cashCard = new CashCard(99L, 123.45);
        assertThat(this.json.write(cashCard)).isStrictlyEqualToJson("expected.json");
        assertThat(this.json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(cashCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(this.json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(this.json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(123.45);
    }

    @Test
    public void cashCardDeserializationTest() throws Exception {
        final String expected = """
                {
                        "id": 99,
                        "amount": 123.45
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }
}
