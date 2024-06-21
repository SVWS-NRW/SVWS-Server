<template>
	<div class="page--content page--content--full gap-2">
		<svws-ui-content-card title="Übersicht aller Unterrichte im Zeitraster" class="page--content-flex-column">
			<svws-ui-table :items="[]" :no-data="false" has-background>
				<template #header>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center" role="columnheader" />
						<div v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="svws-ui-td cursor-pointer svws-divider svws-align-center" role="columnheader" @click="selected = wochentag" :class="{ 'svws-selected bg-red-200': toRaw(selected) === wochentag }">
							{{ wochentag.kuerzel }}
						</div>
					</div>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center" :class="[`col-span-${stundenplanManager().getWochenTypModell()+1}`]" role="columnheader" />
						<template v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id">
							<div class="svws-ui-td cursor-pointer svws-align-center svws-divider" role="columnheader" @click="selected = 0" :class="{ 'svws-selected': toRaw(selected) === 0, }">
								{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(0) }}
							</div>
							<div v-for="wochentyp in stundenplanManager().getWochenTypModell()" :key="wochentyp" class="svws-ui-td cursor-pointer svws-align-center svws-divider" role="columnheader">
								{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(wochentyp) }}
							</div>
						</template>
					</div>
				</template>
				<template #body>
					<div v-for="stunde in stundenplanManager().zeitrasterGetStundenRange()" :key="stunde" role="row" class="svws-ui-tr select-none cursor-pointer">
						<div class="svws-ui-td select-none svws-align-center svws-divider svws-selectable" role="cell" @click="selected = stunde" :class="{ 'svws-selected bg-red-200': toRaw(selected) === stunde, }">
							{{ stunde }}
						</div>
						<div v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="svws-ui-td select-none svws-align-center svws-selectable" role="cell" @click="selected = stunde" :class="{ 'svws-selected': selected === stunde, }">
							<div v-for="wochentyp, i in stundenplanManager().getWochenTypModell()+1" :key="wochentyp" class="cursor-pointer svws-align-center svws-divider">
								{{ stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, i).size() }}
							</div>
						</div>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Unterrichtsliste" class="page--content-flex-column">
			<svws-ui-table :items :no-data="false" has-background>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref, effect, toRaw, computed } from "vue";
	import type { List, StundenplanUnterricht} from "@core";
	import { ArrayList, Wochentag } from "@core";
	import type { StundenplanUnterrichteProps } from "./SStundenplanUnterrichteProps";

	const props = defineProps<StundenplanUnterrichteProps>();

	const selected = ref<number | Wochentag>();

	effect(() => console.log(selected.value))

	const wochentyprange = computed(() => {
		const range = [];
		const modell = props.stundenplanManager().stundenplanGetWochenTypModell();
		for (let i = 0; i <= modell; i++)
			range.push(i);
		return range;
	})

	const items = computed(() => {
		const list: List<StundenplanUnterricht> = new ArrayList();
		if (selected.value instanceof Wochentag)
			for (const stunde of props.stundenplanManager().zeitrasterGetStundenRange())
				for (const wochentyp of wochentyprange.value)
					list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(selected.value, stunde, wochentyp));
		else if (typeof selected.value === 'number')
			for (const wochentag of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange())
				for (const wochentyp of wochentyprange.value)
					list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, selected.value, wochentyp));
		return list;
	})

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full flex flex-col gap-8
	}

</style>
