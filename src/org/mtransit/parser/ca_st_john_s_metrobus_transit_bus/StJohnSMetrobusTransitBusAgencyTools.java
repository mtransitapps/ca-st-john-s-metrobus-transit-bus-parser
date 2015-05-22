package org.mtransit.parser.ca_st_john_s_metrobus_transit_bus;

import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MSpec;
import org.mtransit.parser.mt.data.MTrip;

// http://www.metrobus.com/insidepages.asp
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
		System.out.printf("Generating Metrobus Transit bus data...\n");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("Generating Metrobus Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
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

	@Override
	public long getRouteId(GRoute gRoute) {
		return Long.parseLong(gRoute.route_short_name.trim()); // using route short name as route ID
	}

	@Override
	public String getRouteShortName(GRoute gRoute) {
		return gRoute.route_short_name.trim();
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

	@Override
	public String getRouteColor(GRoute gRoute) {
		int rsn = Integer.parseInt(gRoute.route_short_name.trim());
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
		// @formatter:on
		default:
			System.out.println("Unexpected route color " + gRoute);
			System.exit(-1);
			return null;
		}
	}

	private static final String MARINE_INSTITUTE = "Marine Institute";
	private static final String VIRGINIA_PARK = "Virginia Pk";
	private static final String DOWNTOWN = "Downtown";
	private static final String KELSEY_DR = "Kelsey Dr";
	private static final String SHEA_HEIGHTS = "Shea Hts";
	private static final String INSTITUTE_EXPRESS = "Institute Express";
	private static final String CUCKHOLDS_COVE = "Cuckholds Cv";
	private static final String KENMOUNT_TERRACE = "Kenmount Ter";
	private static final String TORBAY_ROAD = "Torbay Rd";
	private static final String MUN_CENTER = "MUN Ctr";
	private static final String AIRPORT_HEIGHTS = "Airport Hts";
	private static final String MOUNT_PEARL = "Mt Pearl";
	private static final String STAVANGER_DRIVE = "Stavanger Dr";
	private static final String AVALON_MALL = "Avalon Mall";
	private static final String GOULDS = "Goulds";
	private static final String THE_VILLAGE = "The Vlg";
	private static final String MUN_EXPRESS = "MUN Express";

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (mRoute.id == 1l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(MARINE_INSTITUTE, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 2l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(VIRGINIA_PARK, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 3l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(STAVANGER_DRIVE, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 5l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(VIRGINIA_PARK, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 6l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(DOWNTOWN, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 9l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(TORBAY_ROAD, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(MUN_CENTER, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 10l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(DOWNTOWN, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(KELSEY_DR, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 11l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(SHEA_HEIGHTS, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 12l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 13l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(INSTITUTE_EXPRESS, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 14l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(AIRPORT_HEIGHTS, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(MUN_CENTER, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 15l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(CUCKHOLDS_COVE, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 16l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(KENMOUNT_TERRACE, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(MUN_CENTER, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 17l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(TORBAY_ROAD, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(MUN_CENTER, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 18l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(GOULDS, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 19l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 20l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(AIRPORT_HEIGHTS, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 21l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(MOUNT_PEARL, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 22l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(MOUNT_PEARL, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 23l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(STAVANGER_DRIVE, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(AVALON_MALL, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 24l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(MUN_EXPRESS, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 25l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(GOULDS, gTrip.direction_id);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignString(THE_VILLAGE, gTrip.direction_id);
				return;
			}
		} else if (mRoute.id == 26l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignString(MUN_EXPRESS, gTrip.direction_id);
				return;
			}
		}
		System.out.println("Unexpected trip " + gTrip);
		System.exit(-1);
	}

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = MSpec.cleanStreetTypes(tripHeadsign);
		return MSpec.cleanLabel(tripHeadsign);
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

	private static final Pattern BUILDING = Pattern.compile("(building)", Pattern.CASE_INSENSITIVE);
	private static final String BUILDING_REPLACEMENT = "Bldg";

	private static final Pattern CENTER = Pattern.compile("(cent(er|re))", Pattern.CASE_INSENSITIVE);
	private static final String CENTER_REPLACEMENT = "Ctr";

	private static final Pattern PLAZA = Pattern.compile("(plaza)", Pattern.CASE_INSENSITIVE);
	private static final String PLAZA_REPLACEMENT = "Plz";

	@Override
	public String cleanStopName(String gStopName) {
		if (Utils.isUppercaseOnly(gStopName, true)) {
			gStopName = gStopName.toLowerCase(Locale.ENGLISH);
		}
		gStopName = CIVIC_ADDRESS.matcher(gStopName).replaceAll(CIVIC_ADDRESS_REPLACEMENT);
		gStopName = DASH_P1.matcher(gStopName).replaceAll(DASH_P1_REPLACEMENT);
		gStopName = CIVIC_ADDRESS_ENDS_WITH.matcher(gStopName).replaceAll(StringUtils.EMPTY);
		gStopName = DASH.matcher(gStopName).replaceAll(DASH_REPLACEMENT);
		gStopName = AND.matcher(gStopName).replaceAll(AND_REPLACEMENT);
		gStopName = APARTMENT.matcher(gStopName).replaceAll(APARTMENT_REPLACEMENT);
		gStopName = BUILDING.matcher(gStopName).replaceAll(BUILDING_REPLACEMENT);
		gStopName = CENTER.matcher(gStopName).replaceAll(CENTER_REPLACEMENT);
		gStopName = PLAZA.matcher(gStopName).replaceAll(PLAZA_REPLACEMENT);
		gStopName = MSpec.cleanStreetTypes(gStopName);
		gStopName = MSpec.cleanNumbers(gStopName);
		return MSpec.cleanLabel(gStopName);
	}
}
