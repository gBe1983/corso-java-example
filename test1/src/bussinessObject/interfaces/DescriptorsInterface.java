/**
 * 
 */
package bussinessObject.interfaces;

import java.util.List;

/**
 * @author Dr
 *
 */
public interface DescriptorsInterface {
	public void addColumnDescriptor(ColumnDescriptorInterface columnDescriptorInterface);
	public List<ColumnDescriptorInterface> getDescriptors();
}
