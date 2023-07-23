<template>
	<div class="page--content">
		<div v-if="stundenplan === undefined">
			Derzeit liegt kein Stundenplan für diesen Lernabschnitt vor.
		</div>
		<div v-else class="flex flex-col">
			<svws-ui-multi-select title="Stundenplan" v-model="stundenplan_auswahl" :items="mapStundenplaene.values()" span="full"
				:item-text="(s: StundenplanListeEintrag) => s.bezeichnung + ' : ' + toDateStr(s.gueltigAb) + ' - ' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + ' - ' + toKW(s.gueltigBis) + ')'" />
			<div class="mt-2"> <router-view :key="$route.hash" /> </div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { type WritableComputedRef, computed } from "vue";
	import type { LehrerStundenplanAuswahlProps } from "./SLehrerStundenplanAuswahlProps";
	import { DateUtils, DeveloperNotificationException, type StundenplanListeEintrag } from "@core";

	const props = defineProps<LehrerStundenplanAuswahlProps>();

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

</script>