package example.cashcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Arrays;

@JsonTest
class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonArray;

    private CashCard[] cashCards;

    @BeforeEach
    public void setUp() {
        cashCards = Arrays.array(
            new CashCard(99L, 123.45, "sarah1"),
            new CashCard(100L, 1.00, "sarah1"),
            new CashCard(101L, 150.00, "sarah1"));
    }

    @Test
    public void cashCardSerializationTest() throws Exception {
        CashCard cashCard = cashCards[0];
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(123.45);
    }

    @Test
    public void cashCardDeserializationTest() throws Exception {
        final String expected = """
                {
                        "id": 99,
                        "amount": 123.45,
                        "owner": "sarah1"
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45, "sarah1"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }

    @Test
    public void cashCardListSerializationTest() throws Exception {
        assertThat(jsonArray.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    public void cashCardListDeserializationTest() throws Exception {
        final String expected = """
                [
                     {"id": 99, "amount": 123.45 , "owner": "sarah1"},
                     {"id": 100, "amount": 1.00 , "owner": "sarah1"},
                     {"id": 101, "amount": 150.00, "owner": "sarah1"}
                ]
                """;
        assertThat(jsonArray.parse(expected)).isEqualTo(cashCards);
    }
}
