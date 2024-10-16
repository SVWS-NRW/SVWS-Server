<template>
	<div v-if="(klassenListeManager().hasDaten() && (activeRouteType === RouteType.DEFAULT)) || (activeRouteType !== RouteType.DEFAULT)" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeRouteType === RouteType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ klassenListeManager().daten().kuerzel ? 'Klasse ' + klassenListeManager().daten().kuerzel : '—' }}
								<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
									ID: {{ klassenListeManager().daten().id }}
								</svws-ui-badge>
							</span>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</template>
					<template v-else-if="activeRouteType === RouteType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Klasse...</h2>
					</template>
					<template v-else-if="activeRouteType === RouteType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ klassenSubline }}</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager>
			<router-view />
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import type { KlassenAppProps } from "./SKlassenAppProps";
	import { computed } from "vue";
	import { RouteType } from "~/router/RouteType";

	const props = defineProps<KlassenAppProps>();

	const klassenSubline = computed(() => {
		const auswahlKlassenList = props.klassenListeManager().liste.auswahlSorted();
		const leadingKlassenList = [];
		for (let index = 0; index < auswahlKlassenList.size(); index++) {
			if (index > 4)
				break;

			leadingKlassenList.push(auswahlKlassenList.get(index).kuerzel);
		}

		let subline = leadingKlassenList.join(', ');
		if (auswahlKlassenList.size() > 5)
			subline += ` und ${auswahlKlassenList.size() - 5} Weitere`;

		return subline;
	})

	const lehrerkuerzel = computed<string>(() => {
		if (!props.klassenListeManager().hasDaten())
			return '';
		let lehrerkuerzelStr = '';
		for (const lehrerId of props.klassenListeManager().daten().klassenLeitungen) {
			const lehrer = props.klassenListeManager().lehrer.get(lehrerId);
			if (lehrer === null)
				continue;
			lehrerkuerzelStr += (lehrerkuerzelStr.length > 0) ? `, ${lehrer.kuerzel}` : lehrer.kuerzel;
		}
		return lehrerkuerzelStr;
	});

</script>
