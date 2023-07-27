<template>
	<svws-ui-sub-nav>
		<template v-if="stundenplan !== undefined">
			<svws-ui-multi-select title="Stundenplan" v-model="stundenplan_auswahl" :items="mapStundenplaene.values()" headless
				class="w-144 border-l-svws-700 border p-1 pl-2 rounded-md"
				:item-text="(s: StundenplanListeEintrag) => s.bezeichnung + ' : ' + toDateStr(s.gueltigAb) + ' - ' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + ' - ' + toKW(s.gueltigBis) + ')'" />
			<svws-ui-multi-select title="Wochentyp" v-model="wochentypAuswahl" :items="wochentypen()" headless
				class="w-32 border-l-svws-700 border p-1 pl-2 rounded-md"
				:item-text="(wt) => manager().stundenplanGetWochenTypAsString(wt)" />
			<svws-ui-multi-select title="Kalenderwochen" v-model="kwAuswahl" :items="kalenderwochen()" headless
				class="w-96 border-l-svws-700 border p-1 pl-2 rounded-md" removable
				:item-text="(kw) => getKalenderwochenString(kw)" />
		</template>
		<svws-ui-modal-hilfe class="ml-auto"> <hilfe-schueler-stundenplan /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>
	<div class="w-full pl-9 pr-9 pt-8 pb-16">
		<div v-if="stundenplan === undefined">
			Derzeit liegt kein Stundenplan für diesen Lernabschnitt vor.
		</div>
		<div v-else class="flex flex-col">
			<div class="mt-2"> <router-view :key="$route.hash" /> </div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { type WritableComputedRef, computed } from "vue";
	import type { SchuelerStundenplanAuswahlProps } from "./SSchuelerStundenplanAuswahlProps";
	import { ArrayList, DateUtils, DeveloperNotificationException, type StundenplanKalenderwochenzuordnung, type List, type StundenplanListeEintrag } from "@core";

	const props = defineProps<SchuelerStundenplanAuswahlProps>();

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.' ];

	function toDateStr(iso: string) : string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string) : string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

	const stundenplan_auswahl : WritableComputedRef<StundenplanListeEintrag> = computed({
		get: () : StundenplanListeEintrag => {
			if (props.stundenplan === undefined)
				throw new DeveloperNotificationException("Unerwarteter Fehler: Kein Stundenplan an dieser Stelle definiert.");
			return props.stundenplan;
		},
		set: (value : StundenplanListeEintrag) => {
			void props.gotoStundenplan(value);
		}
	});

	function wochentypen(): List<number> {
		let modell = props.manager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	}

	const wochentypAuswahl : WritableComputedRef<number> = computed({
		get: () : number => {
			return props.wochentyp();
		},
		set: (value : number) => {
			void props.gotoWochentyp(value);		}
	});

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.manager().kalenderwochenzuordnungauswahlGetMenge();
	}

	function getKalenderwochenString(kw: StundenplanKalenderwochenzuordnung | undefined): string {
		if (kw === undefined)
			return "&ndash;&ndash;&ndash;";
		return props.manager().kalenderwochenzuordnungGetWocheAsString(kw);
	}

	const kwAuswahl : WritableComputedRef<StundenplanKalenderwochenzuordnung | undefined> = computed({
		get: () : StundenplanKalenderwochenzuordnung | undefined => {
			return props.kalenderwoche();
		},
		set: (value : StundenplanKalenderwochenzuordnung | undefined) => {
			void props.gotoKalenderwoche(value);
		}
	});

</script>