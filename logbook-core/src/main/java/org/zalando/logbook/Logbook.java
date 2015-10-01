package org.zalando.logbook;

/*
 * #%L
 * Logbook: Core
 * %%
 * Copyright (C) 2015 Zalando SE
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface Logbook {

    Optional<Correlator> write(final RawHttpRequest request) throws IOException;

    static Logbook create() {
        return builder().build();
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder {

        private HttpLogFormatter formatter = new DefaultHttpLogFormatter();
        private HttpLogWriter writer = new DefaultHttpLogWriter();
        private Obfuscator headerObfuscator = Obfuscator.none();
        private Obfuscator parameterObfuscator = Obfuscator.none();
        private BodyObfuscator bodyObfuscator = BodyObfuscator.none();

        private Builder() {

        }

        public Builder withFormatter(final HttpLogFormatter newFormatter) {
            this.formatter = requireNonNull(newFormatter, "Formatter");
            return this;
        }

        public Builder withWriter(final HttpLogWriter newWriter) {
            this.writer = requireNonNull(newWriter, "Writer");
            return this;
        }

        public Builder withHeaderObfuscator(final Obfuscator newObfuscator) {
            this.headerObfuscator = requireNonNull(newObfuscator, "Obfuscator");
            return this;
        }

        public Builder withParameterObfuscator(final Obfuscator newObfuscator) {
            this.parameterObfuscator = requireNonNull(newObfuscator, "Obfuscator");
            return this;
        }

        public Builder withBodyObfuscator(final BodyObfuscator newObfuscator) {
            this.bodyObfuscator = requireNonNull(newObfuscator, "Obfuscator");
            return this;
        }

        public Logbook build() {
            return new DefaultLogbook(formatter, writer,
                    new Obfuscation(headerObfuscator, parameterObfuscator, bodyObfuscator));
        }

    }

}
