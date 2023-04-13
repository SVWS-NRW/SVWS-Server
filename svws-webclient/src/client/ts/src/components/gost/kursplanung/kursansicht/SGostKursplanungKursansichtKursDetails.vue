<template>
	<tr :style="{ 'background-color': bgColor }" class="table--row-kursdetail relative z-10">
		<td :colspan="anzahlSpalten" style="padding-top: 0.75rem; padding-bottom: 0.75rem;">
			<div class="flex justify-between items-center gap-2">
				<div class="flex items-center gap-12">
					<s-gost-kursplanung-kursansicht-modal-zusatzkraefte :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
						:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
					<div class="flex items-center text-base">
						<div class="mr-2">Schienen</div>
						<button class="group" @click="del_schiene">
							<i-ri-indeterminate-circle-line class="w-5 h-5 group-hover:hidden" />
							<i-ri-indeterminate-circle-fill class="w-5 h-5 hidden group-hover:inline-block" />
						</button>
						<span class="mx-1">{{ kurs.anzahlSchienen }}</span>
						<button class="group" @click="add_schiene">
							<i-ri-add-circle-line class="w-5 h-5 group-hover:hidden" />
							<i-ri-add-circle-fill class="w-5 h-5 hidden group-hover:inline-block" />
						</button>
					</div>
					<div class="flex items-center text-sm font-bold">
						<div class="mr-2 text-base">Kurse</div>
						<button @click="del_kurs" class="group">
							<i-ri-indeterminate-circle-line class="w-5 h-5 group-hover:hidden" />
							<i-ri-indeterminate-circle-fill class="w-5 h-5 hidden group-hover:inline-block" />
						</button>
						<span class="mx-0.5" />
						<button @click="add_kurs" class="group">
							<i-ri-add-circle-line class="w-5 h-5 group-hover:hidden" />
							<i-ri-add-circle-fill class="w-5 h-5 hidden group-hover:inline-block" />
						</button>
					</div>
				</div>
				<div class="flex items-center gap-2">
					<svws-ui-dropdown v-if="kurseMitKursart.size() > 1" type="icon">
						<template #dropdownButton>Zusammenlegen</template>
						<template #dropdownItems>
							<svws-ui-dropdown-item v-for="k in andereKurse.values()" :key="k.id" class="px-2">
								{{ get_kursbezeichnung(k.id) }}
							</svws-ui-dropdown-item>
						</template>
					</svws-ui-dropdown>
					<svws-ui-button size="small" type="secondary">Aufteilen…</svws-ui-button>
				</div>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, LehrerListeEintrag, List } from "@svws-nrw/svws-core";
	import type { ComputedRef } from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		bgColor: string;
		anzahlSpalten: number;
		kurs: GostBlockungKurs;
		kurseMitKursart: List<GostBlockungsergebnisKurs>;
		mapLehrer: Map<number, LehrerListeEintrag>;
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
		box-shadow: inset 0 -2px 4px 0 rgba(0, 0, 0, 0.1), inset 0 1px 4px 0 rgba(0, 0, 0, 0.1);
	}

</style>