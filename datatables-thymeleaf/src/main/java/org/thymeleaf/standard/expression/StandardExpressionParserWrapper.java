/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.thymeleaf.standard.expression;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.util.Validate;

public class StandardExpressionParserWrapper implements IStandardExpressionParser {

   public StandardExpressionParserWrapper() {
      super();
   }

   /**
    * 
    * @param arguments
    *           arguments
    * @param input
    *           input
    * @return the result
    * @deprecated since 2.1.0. Deprecated in favour of
    *             {@link #parseExpression(org.thymeleaf.Configuration, org.thymeleaf.context.IProcessingContext, String)}
    *             . Will be removed in 3.0.
    */
   @Deprecated
   public Expression parseExpression(final Arguments arguments, final String input) {
      Validate.notNull(arguments, "Arguments cannot be null");
      Validate.notNull(input, "Input cannot be null");
      return (Expression) parseExpression(arguments.getConfiguration(), arguments, input, true);
   }

   /**
    * 
    * @param configuration
    *           the Configuration object for the template execution environment.
    * @param processingContext
    *           the processing context object containing the variables to be
    *           applied to the expression.
    * @param input
    *           the expression to be parsed, as an input String.
    * @return the result
    * @since 2.0.9
    */
   public Expression parseExpression(final Configuration configuration, final IProcessingContext processingContext,
         final String input) {
      Validate.notNull(configuration, "Configuration cannot be null");
      Validate.notNull(processingContext, "Processing Context cannot be null");
      Validate.notNull(input, "Input cannot be null");
      return (Expression) parseExpression(configuration, processingContext, input, true);
   }

   /**
    * 
    * @param arguments
    *           arguments
    * @param input
    *           input
    * @param allowParametersWithoutValue
    *           allowParametersWithoutValue
    * @return the result
    * @deprecated since 2.1.0. Deprecated in favour of
    *             {@link #parseAssignationSequence(org.thymeleaf.Configuration, org.thymeleaf.context.IProcessingContext, String, boolean)}
    *             . Will be removed in 3.0.
    */
   @Deprecated
   public AssignationSequence parseAssignationSequence(final Arguments arguments, final String input,
         final boolean allowParametersWithoutValue) {
      return parseAssignationSequence(arguments.getConfiguration(), arguments, input, allowParametersWithoutValue);
   }

   /**
    * 
    * @param configuration
    *           configuration
    * @param processingContext
    *           processingContext
    * @param input
    *           input
    * @param allowParametersWithoutValue
    *           allowParametersWithoutValue
    * @return the result
    * @since 2.0.9
    */
   public AssignationSequence parseAssignationSequence(final Configuration configuration,
         final IProcessingContext processingContext, final String input, final boolean allowParametersWithoutValue) {
      return AssignationUtils.parseAssignationSequence(configuration, processingContext, input,
            allowParametersWithoutValue);
   }

   /**
    * 
    * @param arguments
    *           arguments
    * @param input
    *           input
    * @return the result
    * @deprecated since 2.1.0. Deprecated in favour of
    *             {@link #parseExpressionSequence(org.thymeleaf.Configuration, org.thymeleaf.context.IProcessingContext, String)}
    *             . Will be removed in 3.0.
    */
   @Deprecated
   public ExpressionSequence parseExpressionSequence(final Arguments arguments, final String input) {
      return parseExpressionSequence(arguments.getConfiguration(), arguments, input);
   }

   /**
    * 
    * @param configuration
    *           configuration
    * @param processingContext
    *           processingContext
    * @param input
    *           input
    * @return the result
    * @since 2.0.9
    */
   public ExpressionSequence parseExpressionSequence(final Configuration configuration,
         final IProcessingContext processingContext, final String input) {
      return ExpressionSequenceUtils.parseExpressionSequence(configuration, processingContext, input);
   }

   /**
    * 
    * @param arguments
    *           arguments
    * @param input
    *           input
    * @return the result
    * @deprecated since 2.1.0. Deprecated in favour of
    *             {@link #parseEach(org.thymeleaf.Configuration, org.thymeleaf.context.IProcessingContext, String)}
    *             . Will be removed in 3.0.
    */
   @Deprecated
   public Each parseEach(final Arguments arguments, final String input) {
      return parseEach(arguments.getConfiguration(), arguments, input);
   }

   /**
    * 
    * @param configuration
    *           configuration
    * @param processingContext
    *           processingContext
    * @param input
    *           input
    * @return the result
    * @since 2.0.9
    */
   public Each parseEach(final Configuration configuration, final IProcessingContext processingContext,
         final String input) {
      return EachUtils.parseEach(configuration, processingContext, input);
   }

   /**
    * 
    * @param arguments
    *           arguments
    * @param input
    *           input
    * @return the result
    * @deprecated since 2.1.0. Deprecated in favour of
    *             {@link #parseFragmentSelection(org.thymeleaf.Configuration, org.thymeleaf.context.IProcessingContext, String)}
    *             . Will be removed in 3.0.
    */
   @Deprecated
   public FragmentSelection parseFragmentSelection(final Arguments arguments, final String input) {
      return parseFragmentSelection(arguments.getConfiguration(), arguments, input);
   }

   /**
    * 
    * @param configuration
    *           configuration
    * @param processingContext
    *           processingContext
    * @param input
    *           input
    * @return the result
    * @since 2.0.9
    */
   public FragmentSelection parseFragmentSelection(final Configuration configuration,
         final IProcessingContext processingContext, final String input) {
      return FragmentSelectionUtils.parseFragmentSelection(configuration, processingContext, input);
   }

   /**
    * 
    * @param configuration
    *           configuration
    * @param processingContext
    *           processingContext
    * @param input
    *           input
    * @return the result
    * @since 2.1.0
    */
   public FragmentSignature parseFragmentSignature(final Configuration configuration,
         final IProcessingContext processingContext, final String input) {
      return FragmentSignatureUtils.parseFragmentSignature(configuration, input);
   }

   static IStandardExpression parseExpression(final Configuration configuration,
         final IProcessingContext processingContext, final String input, final boolean preprocess) {

      final String preprocessedInput = (preprocess ? StandardExpressionPreprocessor.preprocess(configuration,
            processingContext, input) : input);

      if (configuration != null) {
         final IStandardExpression cachedExpression = ExpressionCache.getExpressionFromCache(configuration,
               preprocessedInput);
         if (cachedExpression != null) {
            return cachedExpression;
         }
      }

      final Expression expression = Expression.parse(preprocessedInput.trim());

      
      // if (expression == null) {
      // throw new
      // TemplateProcessingException("Could not parse as expression: \"" + input
      // + "\"");
      // }

      // if (configuration != null) {
      // ExpressionCache.putExpressionIntoCache(configuration,
      // preprocessedInput, expression);
      // }

      return expression;

   }

   @Override
   public String toString() {
      return "Standard Expression Parser";
   }

}
