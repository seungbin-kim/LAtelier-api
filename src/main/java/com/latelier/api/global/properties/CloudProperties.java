package com.latelier.api.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("cloud")
public class CloudProperties {

    private final Aws aws;

    @Getter
    @RequiredArgsConstructor
    public static final class Aws {

        private final Credentials credentials;

        private final S3 s3;

        private final Region region;


        @Getter
        @RequiredArgsConstructor
        public static final class Credentials {

            private final String key;

            private final String secret;

        }

        @Getter
        @RequiredArgsConstructor
        public static final class S3 {

            private final String bucket;

        }

        @Getter
        public static final class Region {

            private final String staticValue;

            public Region(@Name("static") String staticValue) {
                this.staticValue = staticValue;
            }
        }
    }

}
