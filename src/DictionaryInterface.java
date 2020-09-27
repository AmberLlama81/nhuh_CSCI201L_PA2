/**
 ******************************************************************************
 *                    HOMEWORK  cs201
 ******************************************************************************
 *                     Dictionary interface
 ******************************************************************************
 *****************************************************************************

               Do not modify this file.

 *****************************************************************************/

public interface DictionaryInterface<AnyType extends Comparable<AnyType>>
{
	/**
	 * Adds e to the dictionary.
	 */
	public void add(AnyType e);

	/**
	 * Removes e from the dictionary.
	 */
	public void remove(AnyType e);

	/**
	 * Returns true if the dictionary contains an element equal to e,
	 * otherwise - false;
	 */
	public boolean contains(AnyType e);

	/**
	 * Returns the number of elements in the dictionary equal to e.
	 */
	public int frequency(AnyType e);

	/**
	*  Combines the other dictionary with the dictionary.
	*/
	public void combine(Dictionary<AnyType> other);
}
