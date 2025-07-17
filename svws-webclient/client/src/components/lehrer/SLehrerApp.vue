<template>
	<div v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="activeViewType === ViewType.DEFAULT">
					<svws-ui-avatar :src="foto ? `data:image/png;base64, ${foto}` : undefined"
						:alt="foto !== null ? `Foto von ${manager().daten().vorname} ${manager().daten().nachname}` : ''" upload capture
						@image:base64="foto => patch({ foto })" />
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							{{ manager().daten().titel }} {{ manager().daten().vorname }} {{ manager().daten().nachname }}
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ manager().daten().kuerzel }}</span>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Anlegen eines neuen Lehrers...</h2>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<div class="flex flex-row gap-3">
							<h2 class="svws-headline text-ui-brand">Mehrfachauswahl</h2>
							<svws-ui-button v-if="manager().liste.auswahlExists()" size="normal" type="danger" @click="resetSelection">
								Auswahl aufheben
							</svws-ui-button>
						</div>
						<span class="svws-subline">{{ lehrerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-group-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerAppProps } from "./SLehrerAppProps";
	import { useRegionSwitch, ViewType } from "@ui";

	const props = defineProps<LehrerAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const foto = computed<string | null>(() => {
		return props.manager().daten().foto;
	});

	const lehrerSubline = computed(() => {
		const auswahlLehrerList = props.manager().liste.auswahlSorted();
		if (auswahlLehrerList.size() > 5)
			return `${auswahlLehrerList.size()} Lehrer ausgewählt`;
		return [...auswahlLehrerList].map(k => k.kuerzel).join(', ');
	})

	async function resetSelection() {
		await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

</script>
