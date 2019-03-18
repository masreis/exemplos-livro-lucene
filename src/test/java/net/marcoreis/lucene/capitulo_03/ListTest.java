package net.marcoreis.lucene.capitulo_03;

import java.util.function.Predicate;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListTest {
	@Test
	public void test1_Reverse() {
		Integer[] arr = { 1, 2, 3, 4 };
		List<Integer> list = new List<Integer>(arr);
		Integer[] reversed = list.reverse();
		for (int i : reversed) {
			System.out.print(i + " ");
		}
		System.out.println("=====");
	}

	@Test
	public void test2_ReverseString() {
		String[] arr = { "A", "B", "C", "D" };
		List<String> list = new List<String>(arr);
		String[] reversed = list.reverse();
		for (String i : reversed) {
			System.out.print(i + " ");
		}
		System.out.println("=====");
	}

	@Test
	public void test3_Filter() {
		Integer[] arr = { 1, 2, 3, 4 };
		List<Integer> list = new List<Integer>(arr);
		Predicate<Integer> pred = (p) -> p % 2 == 0;
		Integer[] filtered = list.filter(pred);
		for (int i = 0; i < filtered.length; i++) {
			System.out.print(filtered[i] + " ");
		}
		System.out.println("=====");
	}

	@Test
	public void test4_FilterBigger() {
		Integer[] arr = { 1, 2, 3, 4 };
		List<Integer> list = new List<Integer>(arr);
		Predicate<Integer> pred = (p) -> p > 2;
		Integer[] filtered = list.filter(pred);
		for (int i = 0; i < filtered.length; i++) {
			System.out.print(filtered[i] + " ");
		}
		System.out.println("=====");
	}
}
