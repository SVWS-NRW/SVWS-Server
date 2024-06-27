<template>
	<div class="page--content">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button @click="modus = !modus" title="Modus wechseln" type="transparent">
					<span :class="[modus ? 'icon-sm i-ri-play-line' : 'icon-sm i-ri-speed-line']" />
					Modus: <span>{{ modus ? 'Einzeln bearbeiten':'Fortlaufend bearbeiten' }}</span>
				</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<div class="page--content-flex-column">
			Anzahl Zuordnungen:
			<span v-for="wt of stundenplanManager().getWochenTypModell()" :key="wt"> {{ stundenplanManager().stundenplanGetWochenTypAsString(wt) }}: {{ stundenplanManager().kalenderwochenzuordnungGetMengeByWochentyp(wt).size() }}</span>
			<div class="flex gap-1">
				<div v-for="kw, i of stundenplanManager().kalenderwochenzuordnungGetMengeAsList()" :key="i" class="border flex flex-row p-2 cursor-pointer" @click="nextWochentyp(kw)">
					<div>{{ stundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw) }}</div>
					<div class="font-bold size-6">{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(kw.wochentyp) }}</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref } from "vue";
	import type { StundenplanKalenderwochenProps } from "./SStundenplanKalenderwochenProps";
	import type { StundenplanKalenderwochenzuordnung } from "@core";
	import { ArrayList } from "@core";

	const props = defineProps<StundenplanKalenderwochenProps>();

	async function nextWochentyp(kw: StundenplanKalenderwochenzuordnung) {
		const kalenderwochenZuordnung = new ArrayList<StundenplanKalenderwochenzuordnung>();
		const modell = props.stundenplanManager().getWochenTypModell();
		const list = modus.value ? [kw] : props.stundenplanManager().kalenderwochenzuordnungGetMengeAsList();
		for (const e of list) {
			if (e.kw >= kw.kw)
				e.wochentyp = (e.wochentyp + 1 > modell) ? 1 : e.wochentyp + 1;
			kalenderwochenZuordnung.add(e);
		}
		await props.patchKalenderwochenzuordnungen(kalenderwochenZuordnung);
	}

	const modus = ref<boolean>(true);



	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);


</script>

<style lang="postcss" scoped>

	.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full flex flex-col gap-8
	}

</style>
