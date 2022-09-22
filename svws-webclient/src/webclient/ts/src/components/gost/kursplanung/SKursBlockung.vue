<template>
	<tr
		class="border border-[#7f7f7f]/20 px-2 text-left"
		:style="{
			'background-color': bgColor
		}"
	>
		<td class="px-2">
			{{ fachKuerzel }}-{{ art }}{{ kurs.nummer
			}}{{ kurs.suffix ? "-" : "" }}{{ kurs.suffix }}
		</td>
		<td class="text-center">Gr.</td>
		<td class="text-center">Di</td>
		<td
			v-for="(b, i) in belegung"
			:key="i"
			class="border border-[#7f7f7f]/20 text-center"
		>
			<svws-ui-badge
				v-if="b?.schueler.size()"
				size="tiny"
				:variant="locked_kurs ? 'error' : 'highlight'"
			>
				{{ b?.schueler.size() }}
				<i-ri-lock-line
					v-if="locked_kurs"
					class="inline-block text-red-700"
				/>
				<i-ri-lock-unlock-line v-else class="inline-block" />
			</svws-ui-badge>
		</td>
		<td class="text-center">ja</td>
	</tr>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungsergebnisKurs,
		GostFach,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		kurs: {
			type: Object as () => GostBlockungKurs,
			required: true
		},
		belegung: {
			type: Array as () => GostBlockungsergebnisKurs[],
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const gostFach: ComputedRef<GostFach | null> = computed(() => {
		let fach: GostFach | null = null
		if (!app.dataFaecher.daten) return null
		for (const f of app.dataFaecher.daten)
			if (f.id === props.kurs.fach_id) {
				fach = f
				break
			}
		return fach
	});

	const fachKuerzel: ComputedRef<string> = computed(() => {
		return gostFach.value?.kuerzelAnzeige?.toString() || "?";
	});

	const fach: ComputedRef<ZulaessigesFach> = computed(() => {
		return ZulaessigesFach.getByKuerzelASD(gostFach.value?.kuerzel || null);
	});

	const locked_kurs: ComputedRef<boolean> = computed(() => {
		return false;
	});

	const bgColor: ComputedRef<string> = computed(() => {
		const fachgruppe = fach.value?.getFachgruppe();
		if (!fachgruppe) return "#ffffff";
		const rgb =
			(fachgruppe.farbe.getRed() << 16) |
			(fachgruppe.farbe.getGreen() << 8) |
			(fachgruppe.farbe.getBlue() << 0);
		return "#" + (0x1000000 + rgb).toString(16).slice(1);
	});

	const art: ComputedRef<string> = computed(() => {
		switch (props.kurs.kursart) {
			case 1:
				return "LK";
			case 2:
				return "GK";
			case 3:
				return "ZK";
			case 4:
				return "PKJ";
			case 5:
				return "VTF";
			default:
				break;
		}
		return "";
	});

	const schiene: ComputedRef<string> = computed(() => {
		app.dataKursblockungsergebnis.daten?.schienen;
		return "";
	});
</script>
