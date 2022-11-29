<template>
	<template v-if="isVisible">
		<tr v-for="kursart in GostKursart.values()" class="text-left" :style="{ 'background-color': bgColor }">
			<template v-if="kurszahlen.get(kursart.id) === 0 && wahlen.get(kursart.id)" class="text-left" :style="{ 'background-color': bgColor }">
				<td class="px-4 border-y border-[#7f7f7f]/20" colspan="3">
					{{ fach.kuerzel }}-{{ kursart.kuerzel }} </td>
				<td class="px-2 border-y border-[#7f7f7f]/20" colspan="3">
					<svws-ui-button class="" size="small" @click="add_kurs(kursart)">Kurs hinzufügen</svws-ui-button> </td>
				<td class="px-4 border-y border-[#7f7f7f]/20 border-r" :colspan="schienen.size()-2">
					{{ wahlen.get(kursart.id) }} Kurswahlen </td>
				<td class="bg-white"></td>
			</template>
		</tr>
	</template>
</template>

<script setup lang="ts">
	import { List, Vector, GostBlockungKurs, GostBlockungSchiene, GostKursart, GostStatistikFachwahl, GostStatistikFachwahlHalbjahr, HashMap, ZulaessigesFach, GostFach, KurszahlenUndWochenstunden } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		fach: { type: GostStatistikFachwahl, required: true },
		halbjahr: { type: Number, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const kurszahlen: ComputedRef<Map<number, number>> =
		computed(() => {
			const kurszahlen : Map<number, number> = new Map();
			for (const kursart of GostKursart.values())
				kurszahlen.set(kursart.id, 0);
			for (const k of kurse.value) {
				if (k.fach_id !== props.fach.id)
					continue;
				let anzahl = kurszahlen.get(k.kursart);
				anzahl = (anzahl === undefined) ? 1 : anzahl + 1;
				kurszahlen.set(k.kursart, anzahl);
			}
			return kurszahlen;
		});

	const kurse: ComputedRef<List<GostBlockungKurs>> =
		computed(()=>
			app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>())

	const schienen: ComputedRef<List<GostBlockungSchiene>> =
		computed(()=> app.dataKursblockung.datenmanager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>())

	const fach_halbjahr: ComputedRef<GostStatistikFachwahlHalbjahr> =
		computed(() => props.fach.fachwahlen[props.halbjahr] ||	new GostStatistikFachwahlHalbjahr());

	const wahlen: ComputedRef<Map<number, number>> =
		computed(() => {
			const wahlen : Map<number, number> = new Map();
			const gostfach : GostFach | undefined = app.dataFaecher.manager?.get(props.fach.id) || undefined;
			if (gostfach === undefined)
				return wahlen;
			const fach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(gostfach.kuerzel);
			wahlen.set(GostKursart.LK.id, fach_halbjahr.value.wahlenLK);
			wahlen.set(GostKursart.GK.id, fach === ZulaessigesFach.PX || fach === ZulaessigesFach.VX ? 0 : fach_halbjahr.value.wahlenGK);
			wahlen.set(GostKursart.ZK.id, fach_halbjahr.value.wahlenZK);
			wahlen.set(GostKursart.PJK.id, fach === ZulaessigesFach.PX ? fach_halbjahr.value.wahlenGK : 0);
			wahlen.set(GostKursart.VTF.id, fach === ZulaessigesFach.VX ? fach_halbjahr.value.wahlenGK : 0);
			return wahlen;
		});

	const isVisible : ComputedRef<boolean> = computed(() => {
		for (const kursart of GostKursart.values()) {
			const k = kurszahlen.value.get(kursart.id);
			const w = wahlen.value.get(kursart.id);
			if ((k === undefined) || w === undefined)
				return false;
			if ((k === 0) && (w > 0))
				return true;
		}
		return false;
	});

	const bgColor: ComputedRef<string | undefined> =
		computed(() => app.dataFachwahlen.getBgColor(props.fach));

	function add_kurs(art: GostKursart) {
		app.dataKursblockung.add_blockung_kurse(props.fach.id, art.id)
	}

</script>
