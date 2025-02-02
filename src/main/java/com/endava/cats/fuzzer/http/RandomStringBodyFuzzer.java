package com.endava.cats.fuzzer.http;

import com.endava.cats.annotations.HttpFuzzer;
import com.endava.cats.fuzzer.executor.SimpleExecutor;
import com.endava.cats.generator.simple.StringGenerator;
import com.endava.cats.model.FuzzingData;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@HttpFuzzer
public class RandomStringBodyFuzzer extends BaseHttpWithPayloadSimpleFuzzer {
    @Inject
    public RandomStringBodyFuzzer(SimpleExecutor executor) {
        super(executor);
    }

    @Override
    protected String getPayload(FuzzingData data) {
        return StringGenerator.generateRandomString();
    }

    @Override
    protected String getScenario() {
        return "Send a request with an random string body";
    }

    @Override
    public String description() {
        return "send a request with a random string body";
    }
}
