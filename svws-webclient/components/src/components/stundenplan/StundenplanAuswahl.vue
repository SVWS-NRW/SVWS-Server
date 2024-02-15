<template>
	<template v-if="stundenplan !== undefined">
		<div class="svws-ui-stundenplan--auswahl">
			<div class="flex-grow">
				<h3 class="text-headline-md">{{ stundenplan.bezeichnung }}</h3>
				<div class="text-headline-md opacity-50">{{ toDateStr(stundenplan.gueltigAb) + '–' + toDateStr(stundenplan.gueltigBis) + ' (KW ' + toKW(stundenplan.gueltigAb) + '–' + toKW(stundenplan.gueltigBis) + ')' }}</div>
			</div>
			<div class="svws-ui-stundenplan--auswahl--wrapper">
				<svws-ui-button type="secondary" @click.stop="ganzerStundenplanAuswahl = !ganzerStundenplanAuswahl" title="Ganzen Stundenplan anzeigen, auch leere Stunden" class="text-black dark:text-white h-9">
					{{ ganzerStundenplanAuswahl ? 'Keine leeren Stunden':'Alle Stunden' }}
				</svws-ui-button>
				<svws-ui-select title="Wochentyp" v-model="wochentypAuswahl" :items="wochentypen()"
					class="print:hidden"
					:disabled="wochentypen().size() <= 0"
					:item-text="(wt: number) => manager().stundenplanGetWochenTypAsString(wt)" />
				<svws-ui-select title="Kalenderwoche" v-model="kwAuswahl" :items="kalenderwochen()"
					:class="{'print:hidden': !kwAuswahl}"
					removable
					:disabled="wochentypen().size() <= 0"
					:item-text="(kw: StundenplanKalenderwochenzuordnung | undefined) => getKalenderwochenString(kw)" />
				<svws-ui-select title="Stundenplan" v-model="stundenplan_auswahl" :items="mapStundenplaene.values()"
					:disabled="mapStundenplaene.size <= 1"
					class="print:hidden"
					:item-text="(s: StundenplanListeEintrag) => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '–' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '–' + toKW(s.gueltigBis) + ')'" />
			</div>
		</div>
	</template>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { StundenplanAuswahlProps } from "./StundenplanAuswahlProps";
	import type { StundenplanKalenderwochenzuordnung, List, StundenplanListeEintrag } from "@core";
	import { ArrayList, DateUtils, DeveloperNotificationException } from "@core";

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

	const stundenplan_auswahl = computed<StundenplanListeEintrag>({
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

	const ganzerStundenplanAuswahl = computed<boolean>({
		get: () : boolean => props.ganzerStundenplan(),
		set: (value : boolean) => void props.setGanzerStundenplan(value),
	})

	const wochentypAuswahl = computed<number>({
		get: () : number => props.wochentyp(),
		set: (value : number) => void props.gotoWochentyp(value),
	});

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return props.manager().kalenderwochenzuordnungGetMengeAsList();
	}

	function getKalenderwochenString(kw: StundenplanKalenderwochenzuordnung | undefined): string {
		if (kw === undefined)
			return "—";
		return props.manager().kalenderwochenzuordnungGetWocheAsString(kw);
	}

	const kwAuswahl = computed<StundenplanKalenderwochenzuordnung | undefined>({
		get: () : StundenplanKalenderwochenzuordnung | undefined => props.kalenderwoche(),
		set: (value : StundenplanKalenderwochenzuordnung | undefined) => void props.gotoKalenderwoche(value),
	});

</script>

<style lang="postcss">

	.svws-ui-stundenplan--auswahl {
		@apply flex flex-wrap gap-y-5 gap-x-20;
	}

	.svws-ui-stundenplan--auswahl--wrapper {
		@apply flex-grow grid gap-2;
		grid-template-columns: minmax(10rem, 0.5fr) minmax(8rem, 0.5fr) minmax(12rem, 0.75fr) minmax(12rem, 1.5fr);

		@media print {
			grid-template-columns: repeat(auto-fit, minmax(12rem, 1fr));
		}
	}

</style>
