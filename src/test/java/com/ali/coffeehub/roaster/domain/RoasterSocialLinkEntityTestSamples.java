package com.ali.coffeehub.roaster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoasterSocialLinkEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RoasterSocialLinkEntity getRoasterSocialLinkEntitySample1() {
        return new RoasterSocialLinkEntity().id(1L).roasterId(1L).platform(1L).url("url1");
    }

    public static RoasterSocialLinkEntity getRoasterSocialLinkEntitySample2() {
        return new RoasterSocialLinkEntity().id(2L).roasterId(2L).platform(2L).url("url2");
    }

    public static RoasterSocialLinkEntity getRoasterSocialLinkEntityRandomSampleGenerator() {
        return new RoasterSocialLinkEntity()
            .id(longCount.incrementAndGet())
            .roasterId(longCount.incrementAndGet())
            .platform(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString());
    }
}
