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
package org.cacheonix.impl.net.tcp;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import org.cacheonix.impl.util.IOUtils;
import org.cacheonix.impl.util.array.HashSet;
import org.cacheonix.impl.util.array.ObjectProcedure;
import org.cacheonix.impl.util.exception.ExceptionUtils;
import org.cacheonix.impl.util.logging.Logger;

/**
 * A runner of a channel selection cycle.
 */
public class SelectorWorker implements Runnable {

   /**
    * Logger.
    *
    * @noinspection UNUSED_SYMBOL, UnusedDeclaration
    */
   private static final Logger LOG = Logger.getLogger(SelectorWorker.class); // NOPMD

   /**
    * Network timeout in milliseconds.
    */
   protected final long networkTimeoutMillis;

   /**
    * Time the NIO selector should block for while waiting for a channel to become ready, must be greater than zero.
    */
   private final long selectorTimeoutMillis;

   /**
    * Selector.
    */
   protected final Selector selector;


   /**
    * Creates SelectorWorker.
    *
    * @param selector              a selector to run select cycle for.
    * @param networkTimeoutMillis  SO_TIMEOUT.
    * @param selectorTimeoutMillis a time the selector should block for while waiting for a channel to become ready,
    *                              must be greater than zero.
    */
   SelectorWorker(final Selector selector, final long networkTimeoutMillis, final long selectorTimeoutMillis) {

      this.selectorTimeoutMillis = selectorTimeoutMillis;
      this.networkTimeoutMillis = networkTimeoutMillis;
      this.selector = selector;
   }


   /**
    * Runs channel selection cycle.
    */
   public void run() {

      try {

         while (!Thread.currentThread().isInterrupted()) {

            // Wait for channel readiness
            selector.select(selectorTimeoutMillis);

            // Process selection
            processSelection();
         }
      } catch (final InterruptedException e) {

         // Thread was interrupted which means
         // that the shutdown has been initiated
         ExceptionUtils.ignoreException(e, "Shutdown has been initiated");
      } catch (final UnrecoverableAcceptException e) {

         LOG.error("Failed to accept a connection, selector thread terminates: " + e, e);
      } catch (final Exception e) {

         LOG.error("Unexpected error, selector thread terminates: " + e, e);
      } finally {

         IOUtils.closeHard(selector);
      }
   }


   /**
    * Processes the selected keys.
    *
    * @throws InterruptedException         if the processor thread was interrupted.
    * @throws UnrecoverableAcceptException if an unrecoverable error occurred.
    */
   @SuppressWarnings("TooBroadScope")
   protected void processSelection() throws InterruptedException, UnrecoverableAcceptException {

      //
      // Get keys
      //

      final Set<SelectionKey> selectedKeys = selector.selectedKeys();
      final Set<SelectionKey> selectorKeys = selector.keys();

      //
      // Calculate idle keys
      //
      HashSet<SelectionKey> idleKeys = null;
      if (selectorKeys.size() != selectedKeys.size()) {

         idleKeys = new HashSet<SelectionKey>(selectorKeys);
         idleKeys.removeAll(selectedKeys);
      }

      //
      // Handle ready keys to clear the buffers
      //
      if (!selectedKeys.isEmpty()) {

         for (final Iterator<SelectionKey> iterator = selectedKeys.iterator(); iterator.hasNext(); ) {

            // Get next key
            final SelectionKey key = iterator.next();
            iterator.remove();

            // Check if the key is still valid
            if (!key.isValid()) {

               continue;
            }

            // Handle key state

            final KeyHandler keyHandler = (KeyHandler) key.attachment();
            keyHandler.registerActivity();
            keyHandler.handleKey(key);
         }
      }

      //
      // Handle idle keys
      //

      if (idleKeys != null && !idleKeys.isEmpty()) {

         final InterruptedException[] interrupted = new InterruptedException[1];
         idleKeys.forEach(new ObjectProcedure<SelectionKey>() {

            public boolean execute(final SelectionKey idleKey) {

               // Process idle key
               try {

                  final KeyHandler keyHandler = (KeyHandler) idleKey.attachment();
                  keyHandler.handleIdle(idleKey);
               } catch (final InterruptedException e) {

                  // Remember exception
                  interrupted[0] = e;

                  // Stop processing idle keys
                  return false;
               }

               // Continue
               return true;
            }
         });

         // Throw exception if any
         if (interrupted[0] != null) {

            throw interrupted[0];
         }
      }
   }


   public String toString() {

      return "SelectorWorker{" +
              "networkTimeoutMillis=" + networkTimeoutMillis +
              ", selectorTimeoutMillis=" + selectorTimeoutMillis +
              '}';
   }
}
