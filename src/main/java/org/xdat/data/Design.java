/*
 *  Copyright 2014, Enguerrand de Rochefort
 * 
 * This file is part of xdat.
 *
 * xdat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xdat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xdat.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.xdat.data;

import org.xdat.chart.Axis;
import org.xdat.chart.Filter;
import org.xdat.chart.ParallelCoordinatesChart;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Design implements Serializable {

	static final long serialVersionUID = 4L;
	private Map<Parameter, String> stringParameterValues = new HashMap<>(0, 1);
	private Map<Parameter, Float> numericalParameterValues = new HashMap<>(0, 1);
	private int id;
	private Cluster cluster;
	private Map<Filter, Boolean> activationMap = new HashMap<>();
	private boolean insideBounds;
	private boolean selected = false;
	private Color gradientColor = null;
	public Design(int id) {
		this.id = id;
	}

	public void setValue(Parameter param, String parameterValue, DataSheet dataSheet) {
		Optional<Float> parsed = NumberParser.parseNumber(parameterValue);
		if (parsed.isPresent()) {
			this.numericalParameterValues.put(param, parsed.get());
			this.stringParameterValues.remove(param);
		} else {
			param.setNumeric(false, dataSheet);
			this.stringParameterValues.put(param, parameterValue);
			this.numericalParameterValues.remove(param);
		}
	}

	/**
	 * Gets the numeric (double) representation of a value for a given
	 * parameter.
	 * 
	 * @param param
	 *            the parameter for which the value should be returned.
	 * @return the parameter value
	 * @throws IllegalArgumentException
	 *             if the parameter is unknown to the design.
	 */
	public double getDoubleValue(Parameter param) {

		if (stringParameterValues.containsKey(param)) {
			return param.getDoubleValueOf(stringParameterValues.get(param));
		} else if (numericalParameterValues.containsKey(param) && param.isNumeric()) {
			return (numericalParameterValues.get(param));
		} else if (numericalParameterValues.containsKey(param)) {
			return param.getDoubleValueOf(Float.toString(numericalParameterValues.get(param)));
		} else {
			// Enumeration<Parameter> e = parameterValues.keys();
			// log("getDoubleValue: parameterValues.containsKey(param) = "+parameterValues.containsKey(param));
			// while(e.hasMoreElements())
			// log("getDoubleValue: parameterValues has key "+e.nextElement().getName());
			throw new IllegalArgumentException("Unknown parameter " + param.getName());
		}
	}

	public String getStringValue(Parameter param) {
		if (stringParameterValues.containsKey(param)) {
			return (stringParameterValues.get(param));
		} else if (numericalParameterValues.containsKey(param)) {
			return Float.toString(numericalParameterValues.get(param));
		} else {
			throw new IllegalArgumentException("Unknown parameter " + param.getName());
		}
	}

	public void removeParameter(Parameter param) {
		if (stringParameterValues.containsKey(param)) {
			stringParameterValues.remove(param);
		} else if (numericalParameterValues.containsKey(param)) {
			numericalParameterValues.remove(param);
		} else {
			throw new IllegalArgumentException("Unknown parameter " + param.getName());
		}
	}

	public boolean isActive(ParallelCoordinatesChart chart) {
		for (int i = 0; i < chart.getAxisCount(); i++) {
			Filter uf = chart.getAxis(i).getUpperFilter();
			Filter lf = chart.getAxis(i).getLowerFilter();
			if (!this.activationMap.containsKey(uf)) {
				this.activationMap.put(uf, true);
			}
			if (!this.activationMap.containsKey(lf)) {
				this.activationMap.put(lf, true);
			}
			if (chart.getAxis(i).isFilterInverted()) {
				if (!(this.activationMap.get(uf) || this.activationMap.get(lf)))
					return false;
			} else {
				if (!this.activationMap.get(uf))
					return false;
				if (!this.activationMap.get(lf))
					return false;
			}
		}
		return true;
	}

	public void setActive(Filter filter, boolean active) {
		this.activationMap.put(filter, active);
	}

	public void evaluateBounds(ParallelCoordinatesChart chart, DataSheet dataSheet) {
		this.insideBounds = true;
		for (int i = 0; i < chart.getAxisCount(); i++) {
			if (!isInsideBounds(chart.getAxis(i), dataSheet)) {
				this.insideBounds = false;
				return;
			}
		}
	}

	private boolean isInsideBounds(Axis axis, DataSheet dataSheet) {
		double value = this.getDoubleValue(axis.getParameter());
		double max = axis.getMax(dataSheet);
		double min = axis.getMin(dataSheet);
		if (min <= value && value <= max) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInsideBounds(ParallelCoordinatesChart chart) {
		return this.insideBounds;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Color getGradientColor() {
		return gradientColor;
	}

	public void setAxisGradientColor(Color gradientColor) {
		this.gradientColor = gradientColor;
	}
	
	public void removeAxisGradientColor() {
		this.gradientColor = null;
	}

	public boolean hasGradientColor() {
		return (this.gradientColor != null);
	}

	public int computeValuesHash(List<Parameter> parameters) {
		return Objects.hash(parameters.stream()
				.map(p -> new ParameterValue(p, getStringValue(p)))
				.toArray());
	}

	private static class ParameterValue {
		private final Parameter parameter;
		private final String value;

		private ParameterValue(Parameter parameter, String value) {
			this.parameter = parameter;
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ParameterValue that = (ParameterValue) o;
			return Objects.equals(parameter, that.parameter) &&
					Objects.equals(value, that.value);
		}

		@Override
		public int hashCode() {
			return Objects.hash(parameter, value);
		}
	}
}
