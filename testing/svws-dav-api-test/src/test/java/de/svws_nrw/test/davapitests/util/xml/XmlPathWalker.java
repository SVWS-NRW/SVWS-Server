package de.svws_nrw.test.davapitests.util.xml;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import groovy.xml.XmlSlurper;
import io.restassured.path.xml.XmlPath;

/**
 * Diese Klasse basiert auf dem {@link XmlPath} und überschreibt
 * {@link XmlPath#get(String)}, so dass immer vom zuletzt betrachteten
 * XML-Element ausgegangen wird. Dazu bietet sie die {@link #up()} und
 * {@link #up(int)} Methoden, um den im XML-Tree nach oben zu wandern.
 *
 * Es werden Methoden aus XmlPath zusätzlich als *AndUp angeboten, um auf ein
 * Element zu peeken.
 *
 * Darüber hinaus gibt es asserts (bspw. für die Existenz von Elementen), welche
 * in Zusammenhang mit dem XMLPath zugrundeliegenden {@link XmlSlurper} zu
 * unnötig viel, schlecht lesbarem Code beim Testen führen würden.
 */
public final class XmlPathWalker extends XmlPath {

	/**
	 * TODO
	 *
	 * @param string
	 */
	public XmlPathWalker(final String string) {
		super(string);
	}

	private String node = "";

	/**
	 * TODO
	 *
	 * @return the node
	 */
	public String getNode() {
		return this.node;
	}

	/**
	 * TODO
	 *
	 * @return the XmlPathWalker
	 */
	public XmlPathWalker up() {
		final int idx = node.lastIndexOf('.');
		if (idx > 0) {
			this.node = node.substring(0, idx);
		}
		return this;
	}

	@Override
	public <T> T get(final String path) {
		if (node == null || node.isBlank()) {
			this.node = path;
		} else {
			this.node += "." + path;
		}
		return super.get(this.node);
	}

	/**
	 * TODO
	 * @param <T>
	 * @param steps
	 * @return the upper node
	 */
	public <T> T getNodeUp(final int steps) {
		for (int i = 0; i < steps; i++) {
			up();
		}
		return this.get(node);
	}

	/**
	 * TODO
	 */
	public void backToDocRoot() {
		this.node = "";
	}

	/**
	 * TODO
	 * @param path
	 * @return true or false
	 */
	public boolean nodeExistsAndUp(final String path) {
		final var nodeBuff = node;
		final var res = !getBoolean(path + ".isEmpty()");
		this.node = nodeBuff;
		return res;
	}

	/**
	 * TODO
	 * @param string
	 * @return the string
	 */
	public String getStringAndUp(final String string) {
		final String res = getString(string);
		up();
		return res;
	}

	/**
	 * TODO
	 * @param string
	 * @return the int
	 */
	public int getIntAndUp(final String string) {
		final int res = getInt(string);
		up();
		return res;
	}

	/**
	 * TODO
	 * @return the result
	 */
	public TypeSafeMatcher<String> nodeExists() {
		return new TypeSafeMatcher<>() {

			@Override
			protected boolean matchesSafely(final String item) {
				return XmlPathWalker.this.nodeExistsAndUp(item);
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText("node existing");
			}
		};
	}
}
