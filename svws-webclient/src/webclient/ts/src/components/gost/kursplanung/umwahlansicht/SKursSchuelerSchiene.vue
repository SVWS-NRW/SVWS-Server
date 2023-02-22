<template>
	<tr :class="{ 'bg-error': schiene_hat_kollisionen }">
		<td class="border-r-2 border-black/25">
			<div class="flex flex-col py-1">
				<span class="font-bold">{{ schiene_g?.bezeichnung }}</span>
				<span class="text-sm">{{ schiene.kurse.size() }} Kurse</span>
				<span class="text-sm">{{ anzahl_schueler }} Schüler</span>
			</div>
		</td>
		<s-kurs-schueler-schiene-kurs v-for="kurs of getSchieneKurse" :key="kurs.hashCode()" :kurs="kurs" :schueler="selected"
			:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
			:api-status="apiStatus" :allow-regeln="allowRegeln" :add-regel="addRegel" :remove-regel="removeRegel"
			:update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung" />
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { ApiStatus } from "~/components/ApiStatus";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number) => Promise<boolean>;
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schiene: GostBlockungsergebnisSchiene;
		selected: SchuelerListeEintrag;
		apiStatus: ApiStatus;
		allowRegeln: boolean;
	}>();

	const anzahl_schueler: ComputedRef<number> = computed(() => props.getErgebnismanager().getOfSchieneAnzahlSchueler(props.schiene.id));

	const schiene_g: ComputedRef<GostBlockungSchiene | undefined> = computed(() => props.getErgebnismanager().getSchieneG(props.schiene.id))

	const schiene_hat_kollisionen: ComputedRef<boolean> = computed(() => {
		return props.getErgebnismanager().getOfSchieneSchuelermengeMitKollisionen(props.schiene.id).contains(props.selected.id);
	});

	const getSchieneKurse: ComputedRef<Vector<GostBlockungsergebnisKurs>> = computed(()=> props.schiene.kurse)

</script>
