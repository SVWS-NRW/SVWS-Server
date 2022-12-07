<template>
	<tr
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{ 'bg-red-400': kollision && !nichtwahl, 'bg-orange-400': nichtwahl && !kollision, 'bg-gradient-to-r':nichtwahl && kollision,'from-red-400': nichtwahl && kollision, 'to-orange-400': nichtwahl && kollision}"
	>
		<td class="px-2">
			<div class="flex justify-between">
				<span>{{ `${schueler.nachname}, ${schueler.vorname}`}} <svws-ui-badge v-if="schueler.status !== 'Aktiv'" size="tiny" variant="highlight">{{schueler.status}}</svws-ui-badge></span>
				<div v-if="selected_kurs && (selected || verbieten_regel || fixier_regel)">
					<svws-ui-icon class="cursor-pointer" @click.stop="verbieten_regel_toggle" >
						<i-ri-forbid-fill v-if="verbieten_regel" class="inline-block"/>
						<i-ri-forbid-line v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
					<svws-ui-icon class="cursor-pointer" @click.stop="fixieren_regel_toggle" >
						<i-ri-pushpin-fill v-if="fixier_regel" class="inline-block"/>
						<i-ri-pushpin-line  v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
				</div>
				<svws-ui-icon v-if="selected"><i-ri-checkbox-blank-circle-fill class="text-blue-400"/></svws-ui-icon>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostBlockungsergebnisManager, GostKursblockungRegelTyp, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { ComputedRef, computed } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const {schueler} = defineProps({
		schueler: {
			type: SchuelerListeEintrag,
			required: true
		},
		selected: { type: Boolean, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	
	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

	const kollision: ComputedRef<boolean> =
		computed(()=> {
			if (manager.value === undefined)
				return false;
			const kursid = app.listAbiturjahrgangSchueler.filter.kursid;
			if (kursid === undefined)
				return manager.value.getOfSchuelerHatKollision(schueler.id);
			return manager.value.getOfKursSchuelermengeMitKollisionen(kursid).contains(schueler.id);
		});

	const nichtwahl: ComputedRef<boolean> = 
		computed(() => {
			if (manager.value === undefined)
				return false;
			return manager.value.getOfSchuelerHatNichtwahl(schueler.id);
		});

	const selected_kurs: ComputedRef<boolean> =
		computed(()=> app.dataKursblockungsergebnis.active_kurs?.value ? true : false)
	
	const regeln: ComputedRef<List<GostBlockungRegel>> =
		computed(()=> app.dataKursblockung.datenmanager?.getMengeOfRegeln() || new Vector<GostBlockungRegel>())

	const verbieten_regel: ComputedRef<GostBlockungRegel | undefined> =
		computed(() => {
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ
					&& regel.parameter.get(0) === schueler.id)
				return regel})

	const fixier_regel: ComputedRef<GostBlockungRegel | undefined> =
		computed(() => {
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ
					&& regel.parameter.get(0) === schueler.id)
				return regel})

	const fixieren_regel_toggle = () => fixier_regel.value ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen()
	const verbieten_regel_toggle = () => verbieten_regel.value ? verbieten_regel_entfernen() : verbieten_regel_hinzufuegen()

	const regel_speichern = async (regel: GostBlockungRegel) => {
		const kurs = app.dataKursblockungsergebnis.active_kurs?.value
		if (!kurs) return
		regel.parameter.set(0, schueler.id)
		regel.parameter.set(1, kurs.id)
		await app.dataKursblockung.patch_blockung_regel(regel)
	}
	const fixieren_regel_hinzufuegen = async () => {
		const regel = await app.dataKursblockung.add_blockung_regel(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ)
		if (!regel) return
		await regel_speichern(regel)
	}
	const fixieren_regel_entfernen = async () => {
		if (!fixier_regel.value) return
		await app.dataKursblockung.del_blockung_regel(fixier_regel.value.id)
	}
	const verbieten_regel_hinzufuegen = async () => {
		const regel = await app.dataKursblockung.add_blockung_regel(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ)
		if (!regel) return
		await regel_speichern(regel)
	}
	const verbieten_regel_entfernen = async () => {
		if (!verbieten_regel.value) return
		await app.dataKursblockung.del_blockung_regel(verbieten_regel.value.id)
	}
</script>
