<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="activeViewType === ViewType.DEFAULT">
					<svws-ui-avatar :src="daten?.foto ? `data:image/png;base64, ${daten?.foto}` : undefined"
						:alt="daten?.foto ? `Foto ${daten?.vorname} ${daten?.nachname}` : ''" upload capture />
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							{{ daten?.titel }} {{ daten?.vorname }} {{ daten?.nachname }}
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ daten?.id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ daten?.kuerzel }}</span>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Anlegen eines neuen Lehrers...</h2>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ lehrerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-briefcase-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerAppProps } from "./SLehrerAppProps";
	import { ViewType } from "@ui";

	const props = defineProps<LehrerAppProps>();

	const daten = computed(() => props.manager().hasDaten() ? props.manager().daten() : null);

	const lehrerSubline = computed(() => {
		const auswahlLehrerList = props.manager().liste.auswahlSorted();
		if (auswahlLehrerList.size() > 5)
			return `${auswahlLehrerList.size()} Lehrer ausgewählt`;
		return [...auswahlLehrerList].map(k => k.kuerzel).join(', ');
	})

</script>
