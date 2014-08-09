package org.jdbcquery;

/**
 * Generic bean object that can be used in unit tests.
 *
 * @author Troy Histed
 */
public class TestBean {

	String param1;
	Integer param2;
	long param3;
	TestBean bean;

	/**
	 * @return the param1
	 */
	public String getParam1() {
		return this.param1;
	}
	/**
	 * @param param1 the param1 to set
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	/**
	 * @return the param2
	 */
	public Integer getParam2() {
		return this.param2;
	}
	/**
	 * @param param2 the param2 to set
	 */
	public void setParam2(Integer param2) {
		this.param2 = param2;
	}
	/**
	 * @return the param3
	 */
	public long getParam3() {
		return this.param3;
	}
	/**
	 * @param param3 the param3 to set
	 */
	public void setParam3(long param3) {
		this.param3 = param3;
	}
	/**
	 * @return the bean
	 */
	public TestBean getBean() {
		return this.bean;
	}
	/**
	 * @param bean the bean to set
	 */
	public void setBean(TestBean bean) {
		this.bean = bean;
	}
}
