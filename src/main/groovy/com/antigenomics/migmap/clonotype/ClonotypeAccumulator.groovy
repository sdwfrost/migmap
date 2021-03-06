/*
 * Copyright 2014-2015 Mikhail Shugay
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antigenomics.migmap.clonotype

import com.antigenomics.migmap.io.InputPort
import com.antigenomics.migmap.mapping.ReadMapping
import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@CompileStatic
class ClonotypeAccumulator implements InputPort<ReadMapping> {
    final AtomicLong totalCounter = new AtomicLong()
    final ConcurrentHashMap<ClonotypeKey, ClonotypeData> clonotypeMap = new ConcurrentHashMap<>()

    ClonotypeAccumulator() {
    }

    @Override
    void put(ReadMapping readMapping) {
        totalCounter.incrementAndGet()
        ClonotypeData clonotypeData = clonotypeMap.putIfAbsent(new ClonotypeKey(readMapping),
                new ClonotypeData(readMapping))
        if (clonotypeData) {
            clonotypeData.update(readMapping)
        }
    }

    @Override
    void close() {

    }

    long getTotal() {
        totalCounter.get()
    }
}
