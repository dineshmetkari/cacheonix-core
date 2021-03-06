/*
 * Cacheonix Systems licenses this file to You under the LGPL 2.1
 * (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.cacheonix.org/products/cacheonix/license-lgpl-2.1.htm
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cacheonix.impl.cache.distributed.partitioned;

import org.cacheonix.CacheonixException;
import org.cacheonix.impl.util.logging.Logger;

/**
 * RuntimeInvalidObjectException
 * <p/>
 *
 * @author <a href="mailto:simeshev@cacheonix.org">Slava Imeshev</a>
 * @since May 14, 2010 11:21:53 PM
 */
public final class RuntimeInvalidObjectException extends CacheonixException {

   private static final long serialVersionUID = 7860199864742943082L;

   /**
    * Logger.
    *
    * @noinspection UNUSED_SYMBOL, UnusedDeclaration
    */
   private static final Logger LOG = Logger.getLogger(RuntimeInvalidObjectException.class); // NOPMD


   public RuntimeInvalidObjectException(final Throwable e) {

      super(e);
   }


   private RuntimeInvalidObjectException(final String message) {

      super(message);
   }
}
