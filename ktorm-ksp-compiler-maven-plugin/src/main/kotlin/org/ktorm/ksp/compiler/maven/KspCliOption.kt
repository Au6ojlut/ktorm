/*
 * Copyright 2018-2024 the original author or authors.
 *
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
 */

package org.ktorm.ksp.compiler.maven

/**
 * KSP CLI options. Replaces the `KspCliOption` enum that was removed from the KSP public API in KSP2.
 */
public enum class KspCliOption(public val optionName: String) {
    CLASS_OUTPUT_DIR_OPTION("classOutputDir"),
    JAVA_OUTPUT_DIR_OPTION("javaOutputDir"),
    KOTLIN_OUTPUT_DIR_OPTION("kotlinOutputDir"),
    RESOURCE_OUTPUT_DIR_OPTION("resourceOutputDir"),
    CACHES_DIR_OPTION("cachesDir"),
    PROJECT_BASE_DIR_OPTION("projectBaseDir"),
    KSP_OUTPUT_DIR_OPTION("kspOutputDir"),
    PROCESSOR_CLASSPATH_OPTION("processorClasspath"),
    WITH_COMPILATION_OPTION("withCompilation"),
    PROCESSING_OPTIONS_OPTION("apoption"),
}
