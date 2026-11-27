<template>
	<div class="svws-ui-stundenplan svws-ui-stundenplan--mode-planung svws-ohne-zeitachse">
		<div class="svws-ui-stundenplan--head">
			<div class="inline-flex gap-1 items-center justify-center opacity-50 text-sm font-bold pb-0.5" />
			<div v-for="{ wochentag } in tage"
				:key="wochentag.id"
				@click="updateSelected(wochentag)"
				class="svws-wochentag-label group"
				:class="{ 'svws-selected': selected === wochentag }">
				<span class="px-2 py-1 rounded-xs group group-hover:bg-ui-25!"> {{ wochentag.beschreibung }}</span>
			</div>
		</div>
		<div class="svws-ui-stundenplan--body">
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse">
				<div v-for="stunde in stunden"
					:key="stunde"
					@click="updateSelected(stunde)"
					class="svws-ui-stundenplan--stunde text-center justify-center cursor-pointer"
					:class="{ 'svws-selected-stunde': selected === stunde }">
					<div class="text-headline-sm">
						{{ stunde }}.&nbsp;Stunde
					</div>
				</div>
			</div>
			<div class="svws-ui-stundenplan--zeitraster"
				v-for="{ wochentag, eintraege } in tage"
				:key="wochentag.id"
				:class="{ 'svws-selected': selected === wochentag }">
				<template v-for="eintrag in eintraege" :key="eintrag.id">
					<div class="svws-ui-stundenplan--stunde cursor-pointer"
						@click="updateSelected(eintrag)"
						:style="{ gridRowStart: eintrag.stunde, gridRowEnd: eintrag.stunde + 1 }"
						:class="{ 'svws-selected': isEintragSelected(eintrag) }">
						<div class="svws-ui-stundenplan--unterricht">
							<span>{{ eintrag.stunde }}</span>
							<div class="flex content-start">
								{{ DateUtils.gibZeitStringOfMinuten(eintrag.beginn) }}–{{ DateUtils.gibZeitStringOfMinuten(eintrag.ende) }}
							</div>
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
	import type { Wochentag } from "@core/core/types/Wochentag";
	import { DateUtils } from "@core/core/utils/DateUtils";

	const props = defineProps<{
		tage: { wochentag: Wochentag; eintraege: UvZeitrasterEintrag[] }[];
		stunden: number[];
		selected: Wochentag | number | UvZeitrasterEintrag | undefined;
		setSelection: (wert: Wochentag | number | UvZeitrasterEintrag | undefined) => void;
	}>();

	function updateSelected(wert: Wochentag | number | UvZeitrasterEintrag): void {
		props.setSelection(wert === props.selected ? undefined : wert);
	}

	function isEintragSelected(eintrag: UvZeitrasterEintrag): boolean {
		const selected = props.selected;
		if (selected instanceof UvZeitrasterEintrag) {
			return selected.id === eintrag.id;
		}
		return selected === eintrag.stunde;
	}
</script>
