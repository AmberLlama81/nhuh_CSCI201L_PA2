import java.util.ArrayList;
import java.util.Arrays;

/**
 ******************************************************************************
 *                    HOMAnyTypeWORK-2, cs201
 ******************************************************************************
 *                    Amortized Dictionary
 ******************************************************************************
 *
 * Implementation of an Amortized Array-Based Dictionary data structure.
 *
 * This data structure supports duplicates and does *NOT* support storage of
 * null references.
 *
 * Notes:
 * 		-It is *highly* recommended that you begin by reading over all the methods,
 *       all the comments, and all the code that has already been written for you.
 *
 * 		-the specifications provided are to help you understand what the methods
 *       are supposed to accomplish.
 * 		-they are *NOT* descriptions for how you should implement the methods.
 * 		-See the lab documentation & lecture notes for implementation details.
 *
 * 		-Some of the helper methods specify a runtime cost; make sure your
 *       implementation meets that requirement.
 * 		-(Also, obviously, if the lecture notes and/or the lab documentation specifies
 *       a runtime cost for a method, you need to pay attention to that).
 *
 *
 *****************************************************************************/



public class Dictionary<AnyType extends Comparable<AnyType>>  implements DictionaryInterface<AnyType> {
	/*
	 * Keeps track of the number of elements in the dictionary.
	 * Take a look at the implementation of size()
	 */
	private int size;
	/*
	 * The head reference to the linked list of Nodes.
	 * Take a look at the Node class.
	 */
	private Node head;

	/**
	 * Creates an empty dictionary.
	 */
	public Dictionary() {
		size = 0;
		head = null;
	}

	/**
	 * Adds e to the dictionary, thus making contains(e) true.
	 * Increments size so as to ensure size() is correct.
	 */
	public void add(AnyType e) {
		if(e == null) {
			return;
		}
		
		Comparable[] tmpArray = {e};
		Node addition = new Node(0, tmpArray, head);
		head = addition;
		mergeDown();
		size++;
	}

	/**
	 * Removes e from the dictionary.  If contains(e) was formerly false,
	 * it is still false.
	 * Otherwise, decrements size so as to ensure size() is correct.
	 */
	public void remove(AnyType e) {
		if(e == null) {
			return;
		}

		if (!contains(e)) {
			return;
		}
		
		size--;
		
		int headSearch = binarySearch(head.array, e);
		if (headSearch != -1) {
			int headPower = head.power;
			ArrayList<Comparable> tmpList = new ArrayList<Comparable>();
			for (Comparable v1: head.array) {
				tmpList.add(v1);
			}
			tmpList.remove(e);
			
			java.util.Queue<Comparable[]> tmpQueue = splitUp(tmpList.toArray(new Comparable[tmpList.size()]), headPower);
			
			if (tmpQueue.size() == 0) {
				if (head.next != null) {
					head = head.next;
				} else {
					head = null;
				}
				return;
			}
			
			head = head.next;
			for (Comparable[] v1: tmpQueue) {
				int cnt = 0;
				int powerCnt = v1.length;
				while (powerCnt != 1) {
					cnt++;
					powerCnt /= 2;
				}
				Node newNode = new Node(cnt, v1, head);
				head = newNode;
			}
			mergeDown();
		} else {
			Node tmpNode = head.next;
			while (binarySearch(tmpNode.array, e) == -1) {
				tmpNode = tmpNode.next;
			}
			
			ArrayList<Comparable> tmpList = new ArrayList<Comparable>();
			for (Comparable v1: tmpNode.array) {
				tmpList.add(v1);
			}
			tmpList.remove(e);
			Comparable tmpVar = head.array[head.array.length - 1];
			int size = tmpList.size();
			boolean inserted = false;
			int i = 0;
			
			while (!inserted) {
				if (tmpList.get(i).compareTo(tmpVar) >= 0 || i >= size - 1) {
					tmpList.add(i, tmpVar);
					inserted = true;
				} else {
					i++;
				}
			}
			
			java.util.Queue<Comparable[]> tmpQueue = splitUp(head.array, head.power);
			
			tmpNode.array = tmpList.toArray(new Comparable[tmpList.size()]);
			
			if (tmpQueue.size() == 0) {
				if (head.next != null) {
					head = head.next;
				} else {
					head = null;
				}
				return;
			} 
			
			
			head = head.next;
			for (Comparable[] v1: tmpQueue) {
				int cnt = 0;
				int powerCnt = v1.length;
				while (powerCnt != 1) {
					cnt++;
					powerCnt /= 2;
				}
				Node newNode = new Node(cnt, v1, head);
				head = newNode;
			}
			mergeDown();
		}
	}

	/**
	 * Returns true iff the dictionary contains an element equal to e.
	 */
	public boolean contains(AnyType e) {
		if(e == null) {
			return false;
		}

		if (frequency(e) == 0) {
			return false;
		}
		
		return true;
	}

	/**
	 * Returns the number of elements in the dictionary equal to e.
	 * This is logically equivalent to the number of times remove(e) needs to be performed
	 * in order for contains(e) to be false.
	 */
	public int frequency(AnyType e) {
		if(e == null) {
			return 0;
		}

		Node tmp = head;
		int freq = 0;
		while (tmp != null) {
			freq += frequency(tmp.array, e);
			tmp = tmp.next;
		}

		return freq;
	}

	/**
	 * Returns the size of the dictionary.
	 */
	public int size() {
		return size;
	}

	/**
	 * Combines with the other AAD using the algorithm discussed in lecture.
	 *
	 * Formally, the following need to be true after combining an AAD with another AAD:
	 * 		-the resulting dictionary contains an item iff it was contained in either of the two dictionaries
	 * 		-the resulting frequency of any item is the sum of its frequency in the two dictionaries
	 * 		-the resulting size is the sum of the two sizes
	 */
	public void combine (Dictionary<AnyType> other) {
		if (other == null || this == other) {
			return;
		}
		
		Node tmpOthNode = other.head;
		while (tmpOthNode != null) {
			Node newNode = new Node(tmpOthNode.power, tmpOthNode.array, null);
			Node tmpNode = head;
			while (tmpNode.power < newNode.power) {
				tmpNode = tmpNode.next;
			}
			
			newNode.next = tmpNode.next;
			tmpNode.next = newNode;
			size += newNode.array.length;
			
			tmpOthNode = tmpOthNode.next;
		}
		
		mergeDown();
	}

	/**
	 * Returns a helpful string representation of the dictionary.
	 */
	public String toString() {
		Node tmp = head;
		StringBuffer result = new StringBuffer();
		while (tmp != null) {
			result.append(tmp.power);
			result.append(": ");
			result.append(Arrays.toString(tmp.array));
			result.append("\n");
			tmp = tmp.next;
		}
		return result.toString();
	}


	/**
	 * Starting with the smallest array, mergeDown() merges arrays of the same size together until
	 * all the arrays have different size.
	 *
	 * This is very useful for implementing add(e)!!!  See the lecture notes for the theory behind this.
	 */
	private void mergeDown() {
		mergeDownHelp(head);
	}
	
	private void mergeDownHelp(Node n1) {
		Node tmp = n1;
		if (tmp == null || tmp.next == null) {
			return;
		} 
		
		if (tmp.array.length == tmp.next.array.length) {
			tmp.array = merge(tmp.array, tmp.next.array);
			tmp.next = tmp.next.next;
			tmp.power++;
			
			if (tmp.next != null && tmp.next.next != null && tmp.next.array.length == tmp.next.next.array.length) {
				mergeDownHelp(tmp.next);
			} else {
				mergeDownHelp(tmp);
			}
		} else {
			mergeDownHelp(tmp.next);
		}
	}

	/**
	 * Assumes a is sorted.
	 *
	 * contains(a, item) 	= -1, if there is no element of a equal to item
	 * 						= k, otherwise, where a[k] is equal to item
	 *
	 * This is needed for Node's indexOf(e)
	 *
	 * O(log(a.length))
	 */
	@SuppressWarnings("unchecked")
	public static int binarySearch(Comparable[] a, Comparable item) {
		int l = 0;
		int r = a.length - 1;
		while (l <= r) {
			int m = (l + r) / 2;
			if (l == r && a[m].compareTo(item) != 0) {
				return -1;
			} else if (a[m].compareTo(item) < 0) {
				if (l == m) {
					l++;
				} else {
					l = m;
				}
			} else if (a[m].compareTo(item) > 0) {
				if (r == m) {
					r--;
				} else {
					r = m;
				}
			} else {
				return m;
			}
		}
		return -1;
	}

	/**
	 * Assumes a is sorted.
	 *
	 * Returns the number of elements of a equal to item.
	 *
	 * This is needed for Node's frequency(e).
	 *
	 * O(log(a.length) + frequency(item))
	 */
	@SuppressWarnings("unchecked")
	public static int frequency(Comparable[] a, Comparable item) {
		int cnt = 0;
		int firstOccur;
		
		firstOccur = binarySearch(a, item);
		
		if (firstOccur != -1) {
			int tmpInt = firstOccur - 1;
			while (tmpInt >= 0 && a[tmpInt].equals(item)) {
				cnt++;
				tmpInt--;
			}
			
			while (firstOccur < a.length && a[firstOccur].equals(item)) {
				cnt++;
				firstOccur++;
			}
		}
		
		return cnt;
	}

	/**
	 * When a and b are sorted arrays, merge(a,b) returns a sorted array
	 * that has length (a.length+b.length) than contains the elements
	 * of a and the elements of b.
	 *
	 * This is useful for implementing the mergeDown() method.
	 *
	 * O(a.length + b.length)
	 */
	@SuppressWarnings("unchecked")
	public static Comparable[] merge(Comparable[] a, Comparable[] b) {
		int i = 0; 
		int j = 0;
		ArrayList<Comparable> result = new ArrayList<Comparable>();
		while (i < a.length && j < b.length) {
			if (a[i].compareTo(b[j]) < 0) {
				result.add(a[i]);
				i++;
			} else {
				result.add(b[j]);
				j++;
			}
		}
		
		while (i < a.length) {
			result.add(a[i]);
			i++;
		}
		
		while (j < b.length) {
			result.add(b[j]);
			j++;
		}
		
		Comparable[] tmpArray = new Comparable[result.size()];
		return result.toArray(tmpArray);
	}

	/**
	 * Returns base^exponent.  This is useful for implementing splitUp(a,k)
	 */
	private static int power(int base, int exponent) {
		return (int) (Math.pow(base, exponent));
	}

	
	private static int indexHelper(int k) {
		int result = 0;
		for (int i = k - 1; i >= 0; i--) {
			result += power(2, i);
		}
		return result;
	}
	/**
	 * Assumes a.length >= 2^k - 1, for the given k.
	 *
	 * Splits the first (2^k -1) elements of a up into k-1 sorted arrays of
	 * length 2^(k-1), 2^(k-2), ..., 2, 1.
	 * Returns a Queue of these arrays (in the above order, i.e. the one with
	 * length 2^(k-1) is at the front).
	 *
	 * This is useful for implementing remove(e) using the algorithm discussed in class.
	 *
	 * O(a.length)
	 */
	@SuppressWarnings("unchecked")
	public static java.util.Queue<Comparable[]> splitUp(Comparable[] a, int k) {
		/*
		 * We'll just use a LinkedList as a Queue in this fashion.  Take a look at the
		 * API for the java.util.Queue interface.
		 */

		java.util.Queue<Comparable[]> q = new java.util.LinkedList<Comparable[]>();

		for (int i = k - 1; i >= 0; i--) {
			ArrayList<Comparable> tmpList = new ArrayList<Comparable>();
			int floorNum;
			if (i == 0) {
				floorNum = 0;
			} else {
				floorNum = indexHelper(i);
			}
			
			for (int j = 0; j < power(2, i); j++) {
				tmpList.add(a[j + floorNum]);
			}
			
			q.add(tmpList.toArray(new Comparable[tmpList.size()]));
		}

		return q;
	}

	/**
	 * Implementation of the underlying array-based data structure.
	 *
	 * AnyTypeach Node:
	 * 			-knows k, its "power"
	 * 			-has myArray, a sorted array of 2^k elements
	 * 			-knows myNext, the next Node in the linked list of Nodes
	 *
	 * You do *NOT* need to change this class.
	 * It is, however, very important that you understand how it works.
	 * You may add additional methods, although you have been provided with sufficient
	 * functionality needed to implement the dictionary.
	 */
	@SuppressWarnings("unchecked")
	private static class Node {
		private int power;
		private Comparable[] array;
		private Node next;

		/**
		 * Creates an Node with the specified parameters.
		 */
		public Node(int power, Comparable[] array, Node next) {
			this.power = power;
			this.array = array;
			this.next = next;
		}

		/**
		 * Returns 	-1, if there is no element in the array equal to e
		 * 			 k, otherwise, where array[k] is equal to e
		 */
		public int indexOf(Comparable e) {
			return Dictionary.binarySearch(array, e);
		}

		/**
		 * Returns	true, if there is an element in the array equal to e
		 * 			false, otherwise
		 */
		public boolean contains(Comparable e) {
			return indexOf(e) > -1;
		}

		/**
		 * Returns the number of elements in the array equal to e
		 */
		public int frequency(Comparable e) {
			return Dictionary.frequency(array, e);
		}

		/**
		 * Returns a useful representation of this Node.  (Note how this is used by Dictionary's toString()).
		 */
		public String toString() {
			return java.util.Arrays.toString(array);
		}
	}

}


