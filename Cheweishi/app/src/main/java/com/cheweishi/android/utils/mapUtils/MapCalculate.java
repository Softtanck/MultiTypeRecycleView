package com.cheweishi.android.utils.mapUtils;

import com.baidu.mapapi.model.LatLng;

/**
 * 计算两点间距离
 * 
 * @author zhangq
 * 
 */
public class MapCalculate {

	public double getDistance(LatLng p1, LatLng p2) {
		MyPoint a = new MyPoint();
		a.lat = p1.latitude;
		a.lng = p1.longitude;
		MyPoint b = new MyPoint();
		b.lat = p2.latitude;
		b.lng = p2.longitude;

		return wv(a, b);
	}

	public double getDistance(double sLat, double sLng, double eLat, double eLng) {
		MyPoint a = new MyPoint();
		a.lat = sLat;
		a.lng = sLng;
		MyPoint b = new MyPoint();
		b.lat = eLat;
		b.lng = eLng;

		return wv(a, b);
	}

	private double wv(MyPoint a, MyPoint b) {
		if (a == null || b == null) {
			return 0;
		}
		a.lng = ew(a.lng, -180, 180);
		a.lat = lw(a.lat, -74, 74);
		b.lng = ew(b.lng, -180, 180);
		b.lat = lw(b.lat, -74, 74);
		return Td(oi(a.lng), oi(b.lng), oi(a.lat), oi(b.lat));

	}

	private double Td(double a, double b, double c, double d) {
		return 6370996.81 * Math.acos(Math.sin(c) * Math.sin(d) + Math.cos(c)
				* Math.cos(d) * Math.cos(b - a));
	}

	private double oi(double a) {
		return Math.PI * a / 180;
	}

	private double lw(double a, int b, int c) {
		a = max(a, b);
		a = min(a, c);
		return a;
	}

	private double min(double a, int c) {
		if (a < c) {
			return a;
		}
		return c;
	}

	private double max(double a, int b) {
		if (a > b) {
			return a;
		}
		return b;
	}

	private double ew(double a, int b, int c) {
		if (a > c)
			a -= c - b;
		if (a < b)
			a += c - b;
		return a;
	}

	class MyPoint {
		public double lat;
		public double lng;
	}
}
