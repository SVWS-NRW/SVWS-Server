<template>
	<template v-if="kurszahlen.get(kursart.id) === 0 && wahlen.get(kursart.id) && allowRegeln">
		<tr class="text-left" :style="{ 'background-color': bgColor }" :key="kursart.id">
			<td colspan="3">
				{{ fach.kuerzel }}-{{ kursart.kuerzel }}
			</td>
			<td class="text-center" colspan="1">
				{{ wahlen.get(kursart.id) }}
			</td>
			<td :colspan="schienen.size()+2">
				<svws-ui-button class="" type="secondary" size="small" @click="add_kurs(kursart)" title="Kurs hinzufügen">Kurs hinzufügen</svws-ui-button>
			</td>
		</tr>
	</template>
	<template v-else>
		<s-kurs-blockung v-for="kurs in vorhandene_kurse(kursart)" :key="kurs.id" :kurs="kurs" :data-faecher="dataFaecher" :blockung="blockung" :ergebnis="ergebnis"
			:list-lehrer="listLehrer" :map-lehrer="mapLehrer" :allow_regeln="allowRegeln" :schueler-filter="schuelerFilter" />
	</template>
</template>

<script setup lang="ts">

	import { List, Vector, GostBlockungKurs, GostBlockungSchiene, GostKursart, GostStatistikFachwahl, GostStatistikFachwahlHalbjahr, ZulaessigesFach, GostFach, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { injectMainApp, Main } from "~/apps/Main";
	import type { UserConfigKeys } from "~/utils/userconfig/keys"
	import { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		fach: GostStatistikFachwahl;
		kursart: GostKursart;
		dataFaecher: DataGostFaecher;
		halbjahr: number;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
		allowRegeln: boolean;
	}>();

	const main: Main = injectMainApp();

	const kurszahlen: ComputedRef<Map<number, number>> = computed(() => {
		const kurszahlen : Map<number, number> = new Map();
		for (const kursart of GostKursart.values())
			kurszahlen.set(kursart.id, 0);
		for (const k of sorted_kurse.value) {
			if (k.fach_id !== props.fach.id)
				continue;
			let anzahl = kurszahlen.get(k.kursart);
			anzahl = (anzahl === undefined) ? 1 : anzahl + 1;
			kurszahlen.set(k.kursart, anzahl);
		}
		return kurszahlen;
	});

	function vorhandene_kurse(kursart: GostKursart): GostBlockungKurs[] {
		const liste = [];
		for (const kurs of sorted_kurse.value)
			if (kurs.fach_id === props.fach.id && kurs.kursart === kursart.id)
				liste.push(kurs);
		return liste;
	}

	const sort_by: ComputedRef<UserConfigKeys['gost.kursansicht.sortierung']> = computed(() => main.config.user_config.get('gost.kursansicht.sortierung') || 'kursart');

	const sorted_kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => {
		if (props.blockung.datenmanager === undefined)
			return new Vector<GostBlockungKurs>();
		if (sort_by.value === 'kursart')
			return props.blockung.datenmanager.getKursmengeSortiertNachKursartFachNummer()
		else return props.blockung.datenmanager.getKursmengeSortiertNachFachKursartNummer()
	})

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(()=> props.blockung.datenmanager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>())

	const fach_halbjahr: ComputedRef<GostStatistikFachwahlHalbjahr> = computed(() => props.fach.fachwahlen[props.halbjahr] ||	new GostStatistikFachwahlHalbjahr());

	const wahlen: ComputedRef<Map<number, number>> = computed(() => {
		const wahlen : Map<number, number> = new Map();
		const gostfach : GostFach | undefined = props.dataFaecher.manager?.get(props.fach.id) || undefined;
		if (gostfach === undefined)
			return wahlen;
		const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(gostfach.kuerzel);
		wahlen.set(GostKursart.LK.id, fach_halbjahr.value.wahlenLK);
		wahlen.set(GostKursart.GK.id, zulFach === ZulaessigesFach.PX || zulFach === ZulaessigesFach.VX ? 0 : fach_halbjahr.value.wahlenGK);
		wahlen.set(GostKursart.ZK.id, fach_halbjahr.value.wahlenZK);
		wahlen.set(GostKursart.PJK.id, zulFach === ZulaessigesFach.PX ? fach_halbjahr.value.wahlenGK : 0);
		wahlen.set(GostKursart.VTF.id, zulFach === ZulaessigesFach.VX ? fach_halbjahr.value.wahlenGK : 0);
		return wahlen;
	});

	const bgColor: ComputedRef<string | undefined> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzelStatistik).getHMTLFarbeRGBA(1.0));

	function add_kurs(art: GostKursart) {
		void props.blockung.add_blockung_kurse(props.fach.id, art.id);
	}

</script>
