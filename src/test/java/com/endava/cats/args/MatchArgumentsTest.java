package com.endava.cats.args;

import com.endava.cats.model.CatsResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@QuarkusTest
class MatchArgumentsTest {
    private MatchArguments matchArguments;

    @BeforeEach
    void setup() {
        matchArguments = new MatchArguments();
    }

    @Test
    void shouldReturnMatchHttpCodes() {
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", List.of("200", "4XX"));
        List<String> matchdCodes = matchArguments.getMatchResponseCodes();

        Assertions.assertThat(matchdCodes).containsOnly("200", "4XX");
    }


    @Test
    void shouldMatchMatchResponseCodes() {
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", List.of("2XX", "400"));
        Assertions.assertThat(matchArguments.isMatchedResponseCode("200")).isTrue();
        Assertions.assertThat(matchArguments.isMatchedResponseCode("202")).isTrue();
        Assertions.assertThat(matchArguments.isMatchedResponseCode("400")).isTrue();
        Assertions.assertThat(matchArguments.isMatchedResponseCode("404")).isFalse();
    }

    @Test
    void shouldNotMatchMatchResponseCodesWhenBlank() {
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", List.of("2XX", "400"));
        Assertions.assertThat(matchArguments.isMatchedResponseCode("200")).isTrue();
        Assertions.assertThat(matchArguments.isMatchedResponseCode("")).isFalse();
        Assertions.assertThat(matchArguments.isMatchedResponseCode(null)).isFalse();
    }

    @Test
    void shouldMatchLines() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseLines", List.of(200L));
        Assertions.assertThat(matchArguments.isMatchedResponseLines(200)).isTrue();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isTrue();
    }

    @Test
    void shouldNotMatchLines() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseLines", null);
        Assertions.assertThat(matchArguments.isMatchedResponseLines(200)).isFalse();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isFalse();
    }

    @Test
    void shouldMatchWords() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseWords", List.of(200L));
        Assertions.assertThat(matchArguments.isMatchedResponseWords(200)).isTrue();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isTrue();
    }

    @Test
    void shouldNotMatchWords() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseWords", null);
        Assertions.assertThat(matchArguments.isMatchedResponseWords(200)).isFalse();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isFalse();
    }

    @Test
    void shouldMatchSizes() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseSizes", List.of(200L));
        Assertions.assertThat(matchArguments.isMatchedResponseSize(200)).isTrue();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isTrue();
    }

    @Test
    void shouldNotMatchSizes() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseSizes", null);
        Assertions.assertThat(matchArguments.isMatchedResponseSize(200)).isFalse();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isFalse();
    }

    @Test
    void shouldMatchRegex() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{\"err\":\"error 333\"}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseRegex", ".*error.*");
        Assertions.assertThat(matchArguments.isMatchedResponseRegex("error 333")).isTrue();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isTrue();
    }

    @Test
    void shouldNotMatchRegex() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("{\"error\":\"value\"}")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        ReflectionTestUtils.setField(matchArguments, "matchResponseRegex", null);
        Assertions.assertThat(matchArguments.isMatchedResponseRegex("error")).isFalse();
        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {"1,0,0,0,null,false", "0,1,0,0,null,false", "0,0,1,0,null,false", "0,0,0,1,null,false", "0,0,0,0,regex,false", "0,0,0,0,null,true"}, nullValues = "null")
    void shouldReturnMatchArgumentSupplied(long words, long sizes, long lines, long code, String regex, boolean inputMatch) {
        ReflectionTestUtils.setField(matchArguments, "matchResponseSizes", sizes != 0 ? List.of(sizes) : null);
        ReflectionTestUtils.setField(matchArguments, "matchResponseWords", words != 0 ? List.of(words) : null);
        ReflectionTestUtils.setField(matchArguments, "matchResponseLines", lines != 0 ? List.of(lines) : null);
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", code != 0 ? List.of(String.valueOf(code)) : null);
        ReflectionTestUtils.setField(matchArguments, "matchResponseRegex", regex);
        ReflectionTestUtils.setField(matchArguments, "matchInput", inputMatch);


        Assertions.assertThat(matchArguments.isAnyMatchArgumentSupplied()).isTrue();
    }

    @Test
    void shouldReturnNotMatchingArguments() {
        Assertions.assertThat(matchArguments.isAnyMatchArgumentSupplied()).isFalse();
    }

    @Test
    void shouldNotMatchCatsResponse() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200).body("test")
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();

        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isFalse();
    }

    @Test
    void shouldMatchCatsResponse() {
        CatsResponse catsResponse = CatsResponse.builder().responseCode(200)
                .numberOfLinesInResponse(200).numberOfWordsInResponse(200).contentLengthInBytes(200).build();
        ReflectionTestUtils.setField(matchArguments, "matchResponseSizes", List.of(200L));
        ReflectionTestUtils.setField(matchArguments, "matchResponseWords", List.of(200L));
        ReflectionTestUtils.setField(matchArguments, "matchResponseLines", List.of(200L));
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", List.of("200"));

        Assertions.assertThat(matchArguments.isMatchResponse(catsResponse)).isTrue();
    }

    @Test
    void shouldReturnMatchRegexString() {
        ReflectionTestUtils.setField(matchArguments, "matchResponseRegex", "*");
        String expected = matchArguments.getMatchString();
        Assertions.assertThat(expected).isEqualTo(" regex: *");
    }

    @Test
    void shouldReturnMatchResponseCodesString() {
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", List.of("200"));
        String expected = matchArguments.getMatchString();
        Assertions.assertThat(expected).isEqualTo(" response codes: [200]");
    }

    @ParameterizedTest
    @CsvSource({"matchResponseSizes,200,response sizes", "matchResponseLines,200,number of lines", "matchResponseWords,200,number of words"})
    void shouldReturnMatchSizesString(String field, int size, String expectedWord) {
        ReflectionTestUtils.setField(matchArguments, field, List.of(size));
        String expected = matchArguments.getMatchString();
        Assertions.assertThat(expected).isEqualTo(" " + expectedWord + ": [200]");
    }

    @Test
    void shouldReturnEmptyMatchString() {
        String expected = matchArguments.getMatchString();
        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    void shouldReturnAllMatchStrings() {
        ReflectionTestUtils.setField(matchArguments, "matchResponseSizes", List.of(200L));
        ReflectionTestUtils.setField(matchArguments, "matchResponseWords", List.of(200L));
        ReflectionTestUtils.setField(matchArguments, "matchResponseLines", List.of(200L));
        ReflectionTestUtils.setField(matchArguments, "matchResponseCodes", List.of("200"));
        ReflectionTestUtils.setField(matchArguments, "matchResponseRegex", "*");
        String expected = matchArguments.getMatchString();
        Assertions.assertThat(expected).isEqualTo(" response codes: [200], regex: *, number of lines: [200], number of words: [200], response sizes: [200]");
    }

    @ParameterizedTest
    @CsvSource({"true,cool,true", "true,uncool,false", "false,cool,false", "false,uncool,false"})
    void shouldMatchInput(boolean matchInputFlag, String reflectedValue, boolean expected) {
        ReflectionTestUtils.setField(matchArguments, "matchInput", matchInputFlag);
        CatsResponse response = CatsResponse.builder().body("i'm a cool body").build();
        boolean reflected = matchArguments.isInputReflected(response, reflectedValue);
        Assertions.assertThat(reflected).isEqualTo(expected);
    }
}
