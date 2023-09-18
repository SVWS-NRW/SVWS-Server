<template>
	<template v-if="stundenplan !== undefined">
		<div class="svws-ui-stundenplan--auswahl">
			<div class="flex-grow">
				<h3 class="text-headline-md">{{ stundenplan.bezeichnung }}</h3>
				<div class="text-headline-md opacity-50">{{ toDateStr(stundenplan.gueltigAb) + '–' + toDateStr(stundenplan.gueltigBis) + ' (KW ' + toKW(stundenplan.gueltigAb) + '–' + toKW(stundenplan.gueltigBis) + ')' }}</div>
			</div>
			<div class="svws-ui-stundenplan--auswahl--wrapper">
				<svws-ui-multi-select title="Wochentyp" v-model="wochentypAuswahl" :items="wochentypen()"
					class="print:hidden"
					:disabled="wochentypen().size() <= 0"
					:item-text="(wt: number) => manager().stundenplanGetWochenTypAsString(wt)" />
				<svws-ui-multi-select title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()"
					:class="{'print:hidden': !kwAuswahl}"
					removable
					:disabled="wochentypen().size() <= 0"
					:item-text="(kw: StundenplanKalenderwochenzuordnung | undefined) => getKalenderwochenString(kw)" />
				<svws-ui-multi-select title="Stundenplan" v-model="stundenplan_auswahl" :items="mapStundenplaene.values()"
					:disabled="mapStundenplaene.size <= 1"
					class="print:hidden"
					:item-text="(s: StundenplanListeEintrag) => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '–' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '–' + toKW(s.gueltigBis) + ')'" />
			</div>
		</div>
	</template>
</template>

<script setup lang="ts">

	import { type WritableComputedRef, computed } from "vue";
	import type { StundenplanAuswahlProps } from "./StundenplanAuswahlProps";
	import { ArrayList, DateUtils, DeveloperNotificationException, type StundenplanKalenderwochenzuordnung, type List, type StundenplanListeEintrag } from "@core";

	const props = defineProps<StundenplanAuswahlProps>();

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
			void props.gotoWochentyp(value);
		}
	});

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.manager().kalenderwochenzuordnungGetMengeAsList();
	}

	function getKalenderwochenString(kw: StundenplanKalenderwochenzuordnung | undefined): string {
		if (kw === undefined)
			return "—";
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

<style lang="postcss">

	.svws-ui-stundenplan--auswahl {
		@apply flex flex-wrap gap-y-5 gap-x-20;
	}

	.svws-ui-stundenplan--auswahl--wrapper {
		@apply flex-grow grid gap-2;
		grid-template-columns: minmax(12rem, 0.5fr) minmax(12rem, 1fr) minmax(12rem, 1fr);

		@media print {
			grid-template-columns: repeat(auto-fit, minmax(12rem, 1fr));
		}
	}

</style>
