<template>
	<tr
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{ 'bg-red-400': kollision, 'bg-blue-400': selected }"
	>
		<td class="px-2">
			<div class="flex justify-between">
				<span>{{ `${schueler.nachname}, ${schueler.vorname}`}}</span>
				<div v-if="selected_kurs && (selected || verbieten_regel || fixier_regel)">
					<svws-ui-icon class="cursor-pointer" @click="verbieten_regel_toggle" >
						<i-ri-forbid-fill v-if="verbieten_regel" class="inline-block"/>
						<i-ri-forbid-line v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
					<svws-ui-icon class="cursor-pointer" @click="fixieren_regel_toggle" >
						<i-ri-pushpin-fill v-if="fixier_regel" class="inline-block"/>
						<i-ri-pushpin-line  v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
				</div>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursblockungRegelTyp, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { ComputedRef, computed } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const {schueler} = defineProps({
		schueler: {
			type: SchuelerListeEintrag,
			required: true
		},
		kollision: { type: Boolean, required: true },
		selected: { type: Boolean, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

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
