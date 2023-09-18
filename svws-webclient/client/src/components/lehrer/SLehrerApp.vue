<template>
	<template v-if="visible">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<svws-ui-avatar :src="'data:image/png;base64, ' + stammdaten?.foto ?? undefined"
					:alt="(stammdaten !== undefined) && (stammdaten.foto !== null) ? 'Foto ' + stammdaten.vorname + ' ' + stammdaten.nachname : ''" upload capture />
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<template v-if="stammdaten !== undefined">
							{{ stammdaten.titel }} {{ stammdaten.vorname }} {{ stammdaten.nachname }}
						</template>
						<template v-else>
							—
						</template>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ stammdaten?.id || '—' }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ stammdaten?.kuerzel || '—' }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<i-ri-briefcase-line />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { LehrerAppProps } from "./SLehrerAppProps";

	const props = defineProps<LehrerAppProps>();

	const visible: ComputedRef<boolean> = computed(() => props.stammdaten !== undefined);

</script>
