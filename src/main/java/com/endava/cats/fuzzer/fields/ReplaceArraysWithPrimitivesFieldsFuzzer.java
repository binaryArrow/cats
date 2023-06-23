package com.endava.cats.fuzzer.fields;

import com.endava.cats.annotations.FieldFuzzer;
import com.endava.cats.fuzzer.executor.FieldsIteratorExecutor;
import com.endava.cats.fuzzer.fields.base.BaseReplaceFieldsFuzzer;
import com.endava.cats.json.JsonUtils;
import com.endava.cats.model.FuzzingData;
import io.github.ludovicianul.prettylogger.PrettyLogger;
import io.github.ludovicianul.prettylogger.PrettyLoggerFactory;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
@FieldFuzzer
public class ReplaceArraysWithPrimitivesFieldsFuzzer extends BaseReplaceFieldsFuzzer {
    protected final PrettyLogger logger = PrettyLoggerFactory.getLogger(getClass());

    public ReplaceArraysWithPrimitivesFieldsFuzzer(FieldsIteratorExecutor ce) {
        super(ce);
    }

    @Override
    public BaseReplaceFieldsFuzzer.BaseReplaceFieldsContext getContext(FuzzingData data) {
        return BaseReplaceFieldsFuzzer.BaseReplaceFieldsContext.builder()
                .replaceWhat("array")
                .replaceWith("primitive")
                .skipMessage("Fuzzer only runs for arrays")
                .fieldFilter(field -> JsonUtils.isArray(data.getPayload(), field))
                .fuzzValueProducer(schema -> List.of("cats_primitive_string"))
                .build();
    }
}
