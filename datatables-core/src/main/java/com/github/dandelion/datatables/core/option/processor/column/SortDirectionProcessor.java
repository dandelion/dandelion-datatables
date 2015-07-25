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
package com.github.dandelion.datatables.core.option.processor.column;

import java.util.ArrayList;
import java.util.List;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.option.AbstractOptionProcessor;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.util.EnumUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Direction;

/**
 * <p>
 * Processor associated with the {@link DatatableOptions#SORTDIRECTION} option.
 * </p>
 * 
 * @author Thibault Duchateau
 * @see DatatableOptions#SORTDIRECTION
 */
public class SortDirectionProcessor extends AbstractOptionProcessor {

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      String valueAsString = context.getValueAsString();
      if (StringUtils.isNotBlank(valueAsString)) {

         List<Direction> sortDirections = new ArrayList<Direction>();
         String[] sortDirectionArray = valueAsString.split(",");

         for (String direction : sortDirectionArray) {
            try {
               sortDirections.add(Direction.valueOf(direction.toUpperCase().trim()));
            }
            catch (IllegalArgumentException e) {
               StringBuilder sb = new StringBuilder();
               sb.append("'");
               sb.append(valueAsString);
               sb.append("' is not a valid sort direction. Possible values are: ");
               sb.append(EnumUtils.printPossibleValuesOf(Direction.class));
               throw new DandelionException(sb.toString(), e);
            }
         }

         return sortDirections;
      }

      return null;
   }
}