<template>
	<tr
		class="px-2 text-left"
		
		:style="{
			'background-color': bgColor
		}"
	>
		<td class="border border-[#7f7f7f]/20 px-2 whitespace-nowrap" :class="{'border-t-2': kursdifferenz}">
	<div class="flex gap-1">
		<template v-if=" kurs === edit_name ">
			{{ fachKuerzel }}-{{ art }}{{ kurs.nummer }}-
			<svws-ui-text-input 
				v-model="suffix"
				focus headless
				style="width: 2rem"
				@blur="edit_name=undefined"
				@keyup.enter="edit_name=undefined"
			/>
		</template>
		<template v-else>
			<span class="underline decoration-dashed underline-offset-2 cursor-text" @click="edit_name = kurs">
				{{ fachKuerzel }}-{{ art }}{{ kurs.nummer }}{{ kurs.suffix ? "-"+kurs.suffix : "" }}
			</span>
		</template>
	</div>
		</td>
		<td class="border border-[#7f7f7f]/20 text-center" :class="{'border-t-2': kursdifferenz}"><svws-ui-checkbox v-model="koop"></svws-ui-checkbox></td>
		<template v-if="!some_belegung"><td></td></template>
		<template v-if="kursdifferenz">
			<td class="border border-[#7f7f7f]/20 text-center" :class="{'border-t-2': kursdifferenz}" :rowspan="kursdifferenz[0]">{{kursdifferenz[1]}}</td>
		</template>
		<td
			v-for="(b, i) in belegung"
			:key="i"
			class="border border-[#7f7f7f]/20 text-center" :class="{'border-t-2': kursdifferenz}"
		>
			<svws-ui-badge
				v-if="b"
				size="tiny"
				:variant="locked_kurs ? 'error' : 'highlight'"
			>
				{{ b.schueler.size() }}
				<i-ri-lock-line
					v-if="locked_kurs"
					class="inline-block text-red-700"
				/>
				<i-ri-lock-unlock-line v-else class="inline-block" />
			</svws-ui-badge>
		</td>
		<td class="border-none bg-white"></td>
	</tr>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungsergebnisKurs,
		GostFach,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		kurs: {
			type: Object as () => GostBlockungKurs,
			required: true
		},
		belegung: {
			type: Array as () => (GostBlockungsergebnisKurs|undefined)[],
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const edit_name: Ref<GostBlockungKurs|undefined> = ref(undefined)

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

	const koop: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kurs.istKoopKurs.valueOf();
		},
		set(value: boolean) {
			const kurs = app.dataKursblockung.manager?.getKurs(props.kurs.id)
			if (!kurs) return
			kurs.istKoopKurs = Boolean(value);
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

	const some_belegung: Ref<boolean> = computed(()=>props.belegung.some(b=>b))
	const kursdifferenz: ComputedRef<[number, number]|undefined> = computed(()=>{
		const kurse = app.dataKursblockungsergebnis.manager?.getKursSchuelerZuordnungenFuerFach(props.kurs.fach_id)
		if (!kurse) return undefined
		const arr = kurse.toArray(new Array<GostBlockungsergebnisKurs>())
		const filtered = arr.filter(k=>k.kursart === art.value)
		if (!filtered.length) return [-1,-1]
		if (props.belegung.find(k=>k)?.id !== filtered.sort((a,b)=>a.id-b.id)[0].id) return undefined
		if (!filtered.length) return undefined
		if (filtered.length === 2) return [2, Math.abs(filtered[0].schueler.size() - filtered[1].schueler.size())]
		const sorted = filtered.sort((a,b)=>b.schueler.size()-a.schueler.size())
		return [filtered.length, sorted[0].schueler.size() - sorted[sorted.length-1].schueler.size()]
	})
</script>
