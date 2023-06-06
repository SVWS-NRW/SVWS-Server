<template>
	<div :style="{ 'background-color': bgColor }" class="table--row-kursdetail">
		<div class="px-2 py-4 flex gap-12 items-center">
			<s-gost-kursplanung-kursansicht-modal-zusatzkraefte :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
				:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
			<div class="flex items-center gap-1">
				<div class="mr-1">Schienen:</div>
				<svws-ui-button type="icon" @click="del_schiene" size="small">
					<i-ri-subtract-line />
				</svws-ui-button>
				<div class="font-bold mx-1">{{ kurs.anzahlSchienen }}</div>
				<svws-ui-button type="icon" @click="add_schiene" size="small">
					<i-ri-add-line />
				</svws-ui-button>
			</div>
			<div class="flex items-center gap-2">
				<svws-ui-button type="secondary" @click="del_kurs" title="Kurs entfernen">
					Kurs <i-ri-subtract-line />
				</svws-ui-button>
				<svws-ui-button type="secondary" @click="add_kurs" title="Kurs hinzufügen">
					Kurs <i-ri-add-line />
				</svws-ui-button>
			</div>
			<div>
				<!--TODO: richtige daten für v-model, Kursbezeichnung als item-text-->
				<svws-ui-multi-select v-model="undefined"
					title="Zusammenlegen mit…"
					class="placeholder--visible"
					headless
					v-if="kurseMitKursart.size() > 1"
					:item-text="item => item?.id || ''"
					:items="andereKurse.values()" />
			<!--<svws-ui-dropdown v-if="kurseMitKursart.size() > 1" type="icon">
				<template #dropdownButton>Zusammenlegen</template>
				<template #dropdownItems>
					<svws-ui-dropdown-item v-for="k in andereKurse.values()" :key="k.id" class="px-2" @click="combineKurs(kurs, k)">
						{{ get_kursbezeichnung(k.id) }}
					</svws-ui-dropdown-item>
				</template>
			</svws-ui-dropdown>-->
			</div>
			<svws-ui-button size="small" type="secondary" @click="splitKurs(kurs)">Aufteilen</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, LehrerListeEintrag, List } from "@core";
	import type { ComputedRef } from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		anzahlSpalten: number;
		kurs: GostBlockungKurs;
		kurseMitKursart: List<GostBlockungsergebnisKurs>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		bgColor: string;
	}>();

	const andereKurse: ComputedRef<Map<number, GostBlockungsergebnisKurs>> = computed(() => {
		const result = new Map<number, GostBlockungsergebnisKurs>();
		for (const k of props.kurseMitKursart)
			if (k.id !== props.kurs.id)
				result.set(k.id, k);
		return result;
	});

	function get_kursbezeichnung(kurs_id: number): string {
		return props.getDatenmanager().getNameOfKurs(kurs_id);
	}

	async function add_kurs() {
		await props.addKurs(props.kurs.fach_id, props.kurs.kursart)
	}

	async function del_kurs() {
		await props.removeKurs(props.kurs.fach_id, props.kurs.kursart);
	}

	async function add_schiene() {
		await props.addSchieneKurs(props.kurs);
	}

	async function del_schiene() {
		await props.removeSchieneKurs(props.kurs);
	}

</script>

<style lang="postcss">

	.table--row-kursdetail {
		@apply relative z-10 border-b-2 border-black/25;
		box-shadow: inset 0 -4px 5px 0 rgba(0, 0, 0, 0.1);
	}

</style>
