// code by jph
package ch.ethz.idsc.retina.dev.zhkart;

import java.util.Optional;

public interface PutProvider<T> {
  /** @return rank of this provider */
  ProviderRank getProviderRank();

  /** a provider is given the opportunity to command a device on the gokart based on the rank.
   * the provider may "pass", i.e. not commanding the device, by returning Optional.empty().
   * 
   * @return message to send to micro-autobox unless the return value is Optional.empty() */
  Optional<T> putEvent();
}
