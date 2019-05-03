package org.mtransit.parser.ca_st_john_s_metrobus_transit_bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.Utils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;
import org.mtransit.parser.mt.data.MTripStop;

// http://www.metrobus.com/html-default/gtfs.asp
// http://www.metrobustransit.ca/google/google_transit.zip
public class StJohnSMetrobusTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-st-john-s-metrobus-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new StJohnSMetrobusTransitBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("\nGenerating Metrobus Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("\nGenerating Metrobus Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIds != null && this.serviceIds.isEmpty();
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeRoute(GRoute gRoute) {
		return super.excludeRoute(gRoute);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final long RID_ENDS_WITH_A = 10_000L;
	private static final long RID_ENDS_WITH_B = 20_000L;

	@Override
	public long getRouteId(GRoute gRoute) {
		String rsn = gRoute.getRouteShortName().trim();
		if (!Utils.isDigitsOnly(rsn)) {
			Matcher matcher = DIGITS.matcher(rsn);
			if (matcher.find()) {
				int digits = Integer.parseInt(matcher.group());
				if (rsn.endsWith("A")) {
					return digits + RID_ENDS_WITH_A;
				} else if (rsn.endsWith("B")) {
					return digits + RID_ENDS_WITH_B;
				}
			}
			System.out.printf("\nUnexptected route ID for %s!\n", gRoute);
			System.exit(-1);
			return -1l;
		}
		return Long.parseLong(rsn); // using route short name as route ID
	}

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongName();
		routeLongName = CleanUtils.cleanSlashes(routeLongName);
		return CleanUtils.cleanLabel(routeLongName);
	}

	@Override
	public String getRouteShortName(GRoute gRoute) {
		return gRoute.getRouteShortName().trim();
	}

	private static final String AGENCY_COLOR_BROWN = "A19153"; // BROWN (from old logo)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BROWN;

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String COLOR_F6863C = "F6863C";
	private static final String COLOR_26A450 = "26A450";
	private static final String COLOR_8FC74A = "8FC74A";
	private static final String COLOR_F7ACB0 = "F7ACB0";
	private static final String COLOR_933D40 = "933D40";
	private static final String COLOR_8F44AD = "8F44AD";
	private static final String COLOR_00BEF2 = "00BEF2";
	private static final String COLOR_068C83 = "068C83";
	private static final String COLOR_1A5B33 = "1A5B33";
	private static final String COLOR_C4393C = "C4393C";
	private static final String COLOR_691A5C = "691A5C";
	private static final String COLOR_9D6743 = "9D6743";
	private static final String COLOR_467C96 = "467C96";
	private static final String COLOR_ED258F = "ED258F";
	private static final String COLOR_FFCC2C = "FFCC2C";
	private static final String COLOR_ADA425 = "ADA425";
	private static final String COLOR_D6400E = "D6400E";
	private static final String COLOR_A6787A = "A6787A";
	private static final String COLOR_3E4095 = "3E4095";
	private static final String COLOR_363435 = "363435";
	private static final String COLOR_EECE20 = "EECE20";

	@Override
	public String getRouteColor(GRoute gRoute) {
		String rsnS = gRoute.getRouteShortName().trim();
		if (!Utils.isDigitsOnly(rsnS)) {
			if ("3A".equalsIgnoreCase(rsnS)) {
				return COLOR_8FC74A; // same as 3
			} else if ("3B".equalsIgnoreCase(rsnS)) {
				return COLOR_8FC74A; // same as 3
			}
			System.out.printf("\nUnexpected route color %s!\n", gRoute);
			System.exit(-1);
			return null;
		}
		int rsn = Integer.parseInt(rsnS);
		switch (rsn) {
		// @formatter:off
		case 1: return COLOR_F6863C;
		case 2: return COLOR_26A450;
		case 3: return COLOR_8FC74A;
		case 5: return COLOR_F7ACB0;
		case 6: return COLOR_933D40;
		case 9: return COLOR_691A5C;
		case 10: return COLOR_8F44AD;
		case 11: return COLOR_3E4095;
		case 12: return COLOR_00BEF2;
		case 13: return COLOR_068C83;
		case 14: return COLOR_1A5B33;
		case 15: return COLOR_C4393C;
		case 16: return COLOR_691A5C;
		case 17: return COLOR_9D6743;
		case 18: return COLOR_467C96;
		case 19: return COLOR_ED258F;
		case 20: return COLOR_FFCC2C;
		case 21: return COLOR_ADA425;
		case 22: return COLOR_D6400E;
		case 23: return COLOR_A6787A;
		case 24: return COLOR_363435;
		case 25: return COLOR_3E4095;
		case 26: return COLOR_363435;
		case 27: return null; // TODO
		case 30: return COLOR_EECE20;
		// @formatter:on
		default:
			System.out.printf("\nUnexpected route color %s!\n", gRoute);
			System.exit(-1);
			return null;
		}
	}

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop, this);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()), this);
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), gTrip.getDirectionId());
	}

	private static final Pattern STARTS_WITH_AREA = Pattern.compile("(^(([\\w]+[\\.]? )+(\\- ))*)", Pattern.CASE_INSENSITIVE);

	private static final Pattern STARTS_WITH_TO = Pattern.compile("(^(to ))", Pattern.CASE_INSENSITIVE);

	private static final Pattern STARTS_WITH_DASH = Pattern.compile("(^.*( )?(\\- ))", Pattern.CASE_INSENSITIVE);

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = STARTS_WITH_AREA.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		tripHeadsign = STARTS_WITH_TO.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		tripHeadsign = STARTS_WITH_DASH.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		List<String> headsignsValues = Arrays.asList(mTrip.getHeadsignValue(), mTripToMerge.getHeadsignValue());
		if (mTrip.getRouteId() == 1L) {
			if (Arrays.asList( //
					"MUN / CONA / MI", //
					"Institutes" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Institutes", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 27L) {
			if (Arrays.asList( //
					"Sheraton / Quidi Vidi / Delta", //
					"Quidi Vidi / Delta" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Quidi Vidi / Delta", mTrip.getHeadsignId()); // Sheraton / Quidi Vidi / Delta
				return true;
			}
		}
		System.out.printf("\nUnexpected trips to merge: %s & %s!\n", mTrip, mTripToMerge);
		System.exit(-1);
		return false;
	}

	private static final Pattern CIVIC_ADDRESS_ENDS_WITH = Pattern.compile("((\\s)*(\\- opposite|\\- opp|opposite|\\-)(\\s)*$)", Pattern.CASE_INSENSITIVE);
	private static final Pattern DASH_P1 = Pattern.compile("((\\s)*(\\-)(\\s)*(\\())", Pattern.CASE_INSENSITIVE);
	private static final String DASH_P1_REPLACEMENT = " (";
	private static final Pattern CIVIC_ADDRESS = Pattern.compile("(([^\\#]*)#(\\s)*([0-9]{1,5})(.*))", Pattern.CASE_INSENSITIVE);
	private static final String CIVIC_ADDRESS_REPLACEMENT = "$4, $2 $5";

	private static final Pattern DASH = Pattern
			.compile(
					"((\\s)*(at |\\-[^-]*\\-|\\-[\\s]*at|\\-[\\s]*by|\\-[\\s]*east of|\\-[\\s]*near|\\-[\\s]*opp[\\s]*(near)?|\\-[\\s]*west of|\\-|by|east of|west of|opp|near)(\\s)*)",
					Pattern.CASE_INSENSITIVE);
	private static final String DASH_REPLACEMENT = " / ";

	private static final Pattern AND = Pattern.compile("( and )", Pattern.CASE_INSENSITIVE);
	private static final String AND_REPLACEMENT = " & ";

	private static final Pattern APARTMENT = Pattern.compile("(apartment)", Pattern.CASE_INSENSITIVE);
	private static final String APARTMENT_REPLACEMENT = "Apt";

	@Override
	public String cleanStopName(String gStopName) {
		if (Utils.isUppercaseOnly(gStopName, true, true)) {
			gStopName = gStopName.toLowerCase(Locale.ENGLISH);
		}
		gStopName = CIVIC_ADDRESS.matcher(gStopName).replaceAll(CIVIC_ADDRESS_REPLACEMENT);
		gStopName = DASH_P1.matcher(gStopName).replaceAll(DASH_P1_REPLACEMENT);
		gStopName = CIVIC_ADDRESS_ENDS_WITH.matcher(gStopName).replaceAll(StringUtils.EMPTY);
		gStopName = DASH.matcher(gStopName).replaceAll(DASH_REPLACEMENT);
		gStopName = AND.matcher(gStopName).replaceAll(AND_REPLACEMENT);
		gStopName = APARTMENT.matcher(gStopName).replaceAll(APARTMENT_REPLACEMENT);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public int getStopId(GStop gStop) {
		if (Utils.isDigitsOnly(gStop.getStopId())) {
			return Integer.parseInt(gStop.getStopId());
		}
		Matcher matcher = DIGITS.matcher(gStop.getStopId());
		if (matcher.find()) {
			return Integer.parseInt(matcher.group());
		}
		System.out.printf("\nUnexpected stop ID for %s!\n", gStop);
		System.exit(-1);
		return -1;
	}
}
