<template>
	<td :class="[ 'w-12 text-center', { 'cursor-pointer border': moeglich && !bewertet, 'border-[#7f7f7f]/20': moeglich, 'opacity-80': bewertet,
		'cursor-not-allowed': cursorNotAllowed } ]" :style=" { 'background-color': bgColor }" @click.stop="emit('update:click')">
		<template v-if="halbjahr !== undefined">
			<span :class="{'rounded-full px-2 bg-red-400': istFachkombiErforderlich}"> {{ wahl }} </span>
		</template>
		<template v-else>
			{{ wahl }}
		</template>
	</td>
</template>

<script setup lang="ts">

	import { AbiturdatenManager, GostFach, GostFaecherManager, GostHalbjahr, GostJahrgangFachkombination, GostKursart, List, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = withDefaults(defineProps<{
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		fach: GostFach;
		halbjahr?: GostHalbjahr | undefined;
		wahl: string;
		moeglich: boolean;
		bewertet: boolean;
		fachkombiErforderlich?: List<GostJahrgangFachkombination>;
		fachkombiVerboten?: List<GostJahrgangFachkombination>;
	}>(), {
		halbjahr: undefined,
		fachkombiErforderlich: () => new Vector<GostJahrgangFachkombination>(),
		fachkombiVerboten: () => new Vector<GostJahrgangFachkombination>()
	});

	const emit = defineEmits<{
		(e: 'update:click'): void,
	}>();

	const istFachkombiErforderlich: ComputedRef<boolean> = computed(() => {
		if (props.halbjahr === undefined)
			return false;
		for (const kombi of props.fachkombiErforderlich) {
			if (kombi.gueltigInHalbjahr[props.halbjahr.id]) {
				const fach1 = props.faechermanager.get(kombi.fachID1);
				if (!fach1)
					return false;
				const f1 = props.abiturmanager.getFachbelegungByKuerzel(fach1?.kuerzel || null)
				const f2 = props.abiturmanager.getFachbelegungByKuerzel(props.fach.kuerzel)
				const belegung_1 = props.abiturmanager.pruefeBelegungMitKursart(f1, GostKursart.fromKuerzel(kombi.kursart1)!, props.halbjahr)
				const belegung_2 = props.abiturmanager.pruefeBelegungMitKursart(f2, GostKursart.fromKuerzel(kombi.kursart1)!, props.halbjahr);
				if (belegung_2)
					return false;
				return belegung_1 !== belegung_2;
			}
		}
		return false;
	});

	const istFachkombiVerboten: ComputedRef<boolean> = computed(() => {
		if (props.halbjahr === undefined)
			return false;
		for (const kombi of props.fachkombiVerboten) {
			if (kombi.gueltigInHalbjahr[props.halbjahr.id]) {
				const fach = props.faechermanager.get(kombi.fachID1)
				if (!fach)
					return false;
				return props.wahl ? true : false;
			}
		}
		return false;
	});

	const cursorNotAllowed: ComputedRef<boolean> = computed(() =>
		(props.halbjahr === undefined) ?
			!props.moeglich :
			!props.moeglich || props.bewertet || istFachkombiVerboten.value
	);

	const bgColor: ComputedRef<string> = computed(() =>
		((props.halbjahr === undefined) && (!props.moeglich)) || ((props.halbjahr !== undefined) && (!props.moeglich) && (!istFachkombiVerboten.value))
			? 'gray'
			: ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB()
	);

</script>
