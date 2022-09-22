<template>
	<tr
		class="border border-[#7f7f7f]/20 px-2 text-left"
		:style="{
			'background-color': bgColor
		}"
	>
		<!-- Bezeichnung weicht hier ab z.B. S/S statt S/S0 -->
		<td class="px-2">
			{{ fach?.daten.kuerzel }}-{{ art }}{{ kurs.nummer
			}}{{ suffix ? "-" + suffix : "" }}
		</td>
		<td class="text-center">
			<svws-ui-checkbox
				:model-value="kurs.istKoopKurs"
			></svws-ui-checkbox>
		</td>
		<td class="w-36 text-center">
			<svws-ui-text-input v-model="suffix"></svws-ui-text-input>
		</td>
		<td class="w-12 px-2 text-center">
			<svws-ui-text-input
				v-model="wochenstunden"
				type="number"
			></svws-ui-text-input>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { GostBlockungKurs, GostFach, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		kurs: {
			type: Object as () => GostBlockungKurs,
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const fach: ComputedRef<ZulaessigesFach|null> = computed(() => {
		let fach: GostFach|null = null
		if (!app.dataFaecher.daten) return null
		for (const f of app.dataFaecher.daten)
			if (f.id === props.kurs.fach_id) {
				fach = f
				break
			}
		return ZulaessigesFach.getByKuerzelASD(fach?.kuerzel || null)
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

	const wochenstunden: WritableComputedRef<number> = computed({
		get(): number {
			return props.kurs.wochenstunden?.valueOf() || 0;
		},
		set(value: number) {
			const kurs = app.dataKursblockung.manager?.getKurs(props.kurs.id)
			if (!kurs) return
			kurs.wochenstunden = Number(value);
			app.dataKursblockung.patch_kurs(kurs);
		}
	});

	const suffix: WritableComputedRef<string> = computed({
		get(): string {
			return props.kurs.suffix.toString();
		},
		set(value: string) {
			const kurs = app.dataKursblockung.manager?.getKurs(props.kurs.id)
			if (!kurs) return
			kurs.suffix = String(value);
			app.dataKursblockung.patch_kurs(kurs);
		}
	});
</script>
