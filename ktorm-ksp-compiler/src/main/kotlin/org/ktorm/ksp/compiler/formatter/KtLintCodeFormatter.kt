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

package org.ktorm.ksp.compiler.formatter

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.api.Code
import com.pinterest.ktlint.rule.engine.api.EditorConfigDefaults
import com.pinterest.ktlint.rule.engine.api.KtLintRuleEngine
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.ruleset.standard.rules.FunctionSignatureRule.Companion.FORCE_MULTILINE_WHEN_PARAMETER_COUNT_GREATER_OR_EQUAL_THAN_PROPERTY
import org.ec4j.core.EditorConfigLoader
import org.ec4j.core.PropertyTypeRegistry
import org.ec4j.core.Resource.Resources
import org.ec4j.core.model.Version
import java.util.*

internal class KtLintCodeFormatter(val environment: SymbolProcessorEnvironment) : CodeFormatter {

    private val propertyRegistry = PropertyTypeRegistry
        .builder()
        .defaults()
        .apply {
            setOf(FORCE_MULTILINE_WHEN_PARAMETER_COUNT_GREATER_OR_EQUAL_THAN_PROPERTY.type).forEach { type(it) }
        }.build()

    private val ktLintRuleEngine =
        KtLintRuleEngine(
            ruleProviders = ServiceLoader
                .load(RuleSetProviderV3::class.java, javaClass.classLoader)
                .flatMap { it.getRuleProviders() }
                .toSet(),
            editorConfigDefaults = EditorConfigDefaults(
                EditorConfigLoader.of(Version.CURRENT, propertyRegistry).load(
                    Resources.ofClassPath(javaClass.classLoader, "/ktorm-ksp-compiler/.editorconfig", Charsets.UTF_8)
                )
            )
        )

    override fun format(fileName: String, code: String): String {
        try {
            // Manually fix some code styles before formatting.
            val snippet = code
                .replace(Regex("""\(\s*"""), "(")
                .replace(Regex("""\s*\)"""), ")")
                .replace(Regex(""",\s*"""), ", ")
                .replace(Regex(""",\s*\)"""), ")")
                .replace(Regex("""\s+get\(\)\s="""), " get() =")
                .replace(Regex("""\s+=\s+"""), " = ")
                .replace("import org.ktorm.ksp.`annotation`", "import org.ktorm.ksp.annotation")

            return ktLintRuleEngine.format(
                Code.fromSnippet(snippet),
                callback = { AutocorrectDecision.ALLOW_AUTOCORRECT })
        } catch (e: Throwable) {
            environment.logger.exception(e)
            return code
        }
    }
}
