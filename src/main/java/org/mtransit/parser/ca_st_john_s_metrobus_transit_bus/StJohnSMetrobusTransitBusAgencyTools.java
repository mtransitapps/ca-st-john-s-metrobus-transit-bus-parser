package org.mtransit.parser.ca_st_john_s_metrobus_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.StringUtils;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mtransit.parser.StringUtils.EMPTY;

// https://www.metrobus.com/html-default/gtfs.asp
// https://www.metrobus.com/html-default/documents.asp
// https://www.metrobus.com/google/google_transit.zip
public class StJohnSMetrobusTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-st-john-s-metrobus-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new StJohnSMetrobusTransitBusAgencyTools().start(args);
	}

	@Nullable
	private HashSet<Integer> serviceIdInts;

	@Override
	public void start(@NotNull String[] args) {
		MTLog.log("Generating Metrobus Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIdInts = extractUsefulServiceIdInts(args, this, true);
		super.start(args);
		MTLog.log("Generating Metrobus Transit bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIdInts != null && this.serviceIdInts.isEmpty();
	}

	@Override
	public boolean excludeCalendar(@NotNull GCalendar gCalendar) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarInt(gCalendar, this.serviceIdInts);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(@NotNull GCalendarDate gCalendarDates) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarDateInt(gCalendarDates, this.serviceIdInts);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		if (this.serviceIdInts != null) {
			return excludeUselessTripInt(gTrip, this.serviceIdInts);
		}
		return super.excludeTrip(gTrip);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final long RID_ENDS_WITH_A = 10_000L;
	private static final long RID_ENDS_WITH_B = 20_000L;

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
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
			throw new MTLog.Fatal("Unexpected route ID for %s!", gRoute);
		}
		return Long.parseLong(rsn); // using route short name as route ID
	}

	@NotNull
	@Override
	public String getRouteLongName(@NotNull GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongNameOrDefault();
		routeLongName = CleanUtils.cleanSlashes(routeLongName);
		return CleanUtils.cleanLabel(routeLongName);
	}

	@Nullable
	@Override
	public String getRouteShortName(@NotNull GRoute gRoute) {
		return gRoute.getRouteShortName().trim();
	}

	private static final String AGENCY_COLOR_BROWN = "A19153"; // BROWN (from old logo)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BROWN;

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@SuppressWarnings("DuplicateBranchesInSwitch")
	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		String routeColor = gRoute.getRouteColor();
		if ("FF0000".equalsIgnoreCase(routeColor)) {
			routeColor = null;
		}
		if (StringUtils.isEmpty(routeColor)) {
			String rsnS = gRoute.getRouteShortName().trim();
			if (!Utils.isDigitsOnly(rsnS)) {
				if ("3A".equalsIgnoreCase(rsnS)) {
					return "8FC74A"; // same as 3
				} else if ("3B".equalsIgnoreCase(rsnS)) {
					return "8FC74A"; // same as 3
				}
				throw new MTLog.Fatal("Unexpected route color %s!", gRoute);
			}
			int rsn = Integer.parseInt(rsnS);
			switch (rsn) {
			// @formatter:off
			case 1: return "F6863C";
			case 2: return "26A450";
			case 3: return "8FC74A";
			case 5: return "F7ACB0";
			case 6: return "933D40";
			case 9: return "691A5C";
			case 10: return "8F44AD";
			case 11: return "3E4095";
			case 12: return "00BEF2";
			case 13: return "068C83";
			case 14: return "1A5B33";
			case 15: return "C4393C";
			case 16: return "691A5C";
			case 17: return "9D6743";
			case 18: return "467C96";
			case 19: return "ED258F";
			case 20: return "FFCC2C";
			case 21: return "ADA425";
			case 22: return "D6400E";
			case 23: return "A6787A";
			case 24: return "363435";
			case 25: return "3E4095";
			case 26: return "363435";
			// case 27: return null; // TO DO
			case 30: return "EECE20";
			// @formatter:on
			default:
				throw new MTLog.Fatal("Unexpected route color %s!", gRoute);
			}
		}
		return super.getRouteColor(gRoute);
	}

	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		mTrip.setHeadsignString(
				cleanTripHeadsign(gTrip.getTripHeadsignOrDefault()),
				gTrip.getDirectionIdOrDefault()
		);
	}

	private static final Pattern _DASH_ = Pattern.compile("([\\s]*-[\\s]*)", Pattern.CASE_INSENSITIVE);
	private static final String _DASH_REPLACEMENT = " - ";

	private static final Pattern STARTS_WITH_DASH_ = Pattern.compile("(^.*( - ))", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CleanUtils.keepToAndRemoveVia(tripHeadsign);
		tripHeadsign = _DASH_.matcher(tripHeadsign).replaceAll(_DASH_REPLACEMENT);
		tripHeadsign = STARTS_WITH_DASH_.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = CleanUtils.cleanBounds(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		tripHeadsign = BAY_ROAD_.matcher(tripHeadsign).replaceAll(BAY_ROAD_REPLACEMENT); // after
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		List<String> headsignsValues = Arrays.asList(mTrip.getHeadsignValue(), mTripToMerge.getHeadsignValue());
		if (mTrip.getRouteId() == 1L) {
			if (Arrays.asList( //
					"MUN / CONA / MI", //
					"Institutes" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Institutes", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 2L) {
			if (Arrays.asList( //
					"Vlg", //
					"Vlg Mall" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Vlg Mall", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 3L) {
			if (Arrays.asList( //
					"Highland Dr / Kingsbridge / Vlg", //
					"Vlg Mall" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Vlg Mall", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 11L) {
			if (Arrays.asList( //
					EMPTY, //
					"Avalon Mall" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Avalon Mall", mTrip.getHeadsignId());
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
		throw new MTLog.Fatal("Unexpected trips to merge: %s & %s!", mTrip, mTripToMerge);
	}

	private String[] getIgnoredWords() {
		return new String[]{
				"AL", "CBC", "CNIB", "EMCO", "HMP", "HSC", "JB", "JJ", "MFRC", "MUN", "MVR", "NL", "RDM", "RCMP", "RNC", "YMCA",
		};
	}

	private static final Pattern NUMBER_SIGN_ = Pattern.compile("(#\\s*(\\d+))", Pattern.CASE_INSENSITIVE);
	private static final String NUMBER_SIGN_REPLACEMENT = "#$2";

	private static final Pattern BAY_ROAD_ = Pattern.compile("(b:rd)", Pattern.CASE_INSENSITIVE);
	private static final String BAY_ROAD_REPLACEMENT = "Bay Rd";

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, gStopName, getIgnoredWords());
		gStopName = _DASH_.matcher(gStopName).replaceAll(_DASH_REPLACEMENT);
		gStopName = NUMBER_SIGN_.matcher(gStopName).replaceAll(NUMBER_SIGN_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanBounds(gStopName);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName); // before
		gStopName = BAY_ROAD_.matcher(gStopName).replaceAll(BAY_ROAD_REPLACEMENT); // after
		return CleanUtils.cleanLabel(gStopName);
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public int getStopId(@NotNull GStop gStop) {
		//noinspection deprecation
		final String stopId = gStop.getStopId();
		if (Utils.isDigitsOnly(stopId)) {
			return Integer.parseInt(stopId);
		}
		Matcher matcher = DIGITS.matcher(stopId);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group());
		}
		throw new MTLog.Fatal("Unexpected stop ID for %s!", gStop);
	}
}
