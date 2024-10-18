<template>
	<Story title="Design Tokens & Farben" id="color" group="top" icon="ri:palette-line" :responsive-disabled="true" :layout="{type: 'grid', width: '90%'}">
		<Variant title="Info" source=" " id="info">
			<div>
				<div class="mb-2 flex flex-col gap-2 bg-ui text-ui">
					<p>
						Um eine konsistente Farbgebung zu gewährleisten, verwenden wir ein Farbschema, das auf Design Tokens basiert. Die unten aufgeführten Tokens sind als CSS-Classes verfügbar und können in Komponenten und dem Client verwendet werden.
					</p>
					<p>
						Es sollen ausschließlich die hier aufgeführten Tokens verwendet werden. Die alten CSS-Color-Classes sollten nicht mehr verwendet werden. Sie sind nur noch in alten Komponenten und Designs vorhanden und werden ersetzt.
					</p>
					<p>
						Bei Bedarf kann das System durch weitere Farben bzw. Farbabstufungen und -kombinationen erweitert werden. In den meisten Fällen sollte für jede Anforderung jedoch eine passende Kombination aus den vorhandenen Tokens gefunden werden können.
					</p>
					<p>
						Durch die Verwendung dieser Tokens können wir sicherstellen, dass die Farben in allen Anwendungen und Komponenten konsistent sind und sich einfach global anpassen lassen. Auch z.B. der Darkmode wird so ohne separate Gestaltung für jede Farbe automatisch unterstützt und ist optimiert.
					</p>
				</div>
				<div class="flex gap-2 font-mono mt-5">
					<div class="flex flex-col">
						<div><strong>[type]</strong></div>
						<div v-for="(type, typeIndex) in semantics.type" :key="typeIndex">{{ type }}</div>
					</div>
					<div class="flex flex-col">
						<div><strong><span class="opacity-50">-ui</span></strong></div>
					</div>
					<div class="flex flex-col">
						<div><strong>-[role]</strong></div>
						<div><em>default</em></div>
						<div v-for="(role, roleIndex) in semantics.role" :key="roleIndex">{{ role }}</div>
					</div>
					<div class="flex flex-col">
						<div><strong>-[prominence]</strong></div>
						<div><em>default</em></div>
						<div v-for="(prominence, prominenceIndex) in semantics.prominence" :key="prominenceIndex">{{ prominence }}</div>
					</div>
					<div class="flex flex-col">
						<div><strong>-[interaction]</strong></div>
						<div><em>default</em></div>
						<div v-for="(interaction, interactionIndex) in semantics.interaction" :key="interactionIndex">{{ interaction }}</div>
					</div>
				</div>
			</div>
		</Variant>
		<Variant title="Design Tokens" source=" " id="tokens">
			<div class="relative">
				<div class="flex flex-col gap-4 font-mono">
					<details v-for="(type, typeIndex) in semantics.type" :key="typeIndex" :open="type === 'bg'">
						<summary>{{ type }}</summary>
						<div v-for="(role, roleIndex) in semantics.role" :key="roleIndex" style="padding: 0.5em 0;">
							<div v-for="(prominence, prominenceIndex) in semantics.prominence" :key="prominenceIndex">
								<div v-for="(interaction, interactionIndex) in semantics.interaction" :key="interactionIndex">
									<div class="grid grid-cols-2 gap-1 items-center">
										<template v-if="shouldExist(type, role, prominence, interaction)">
											<div class="ui-docs-bg flex items-center gap-1"
												:style="{
													backgroundColor: `${role.startsWith('on') ? `var(--color-bg-ui-${role.replace('on', '')})` : ''}`,
													color: `${role.startsWith('on') ? `var(--color-${type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })` : ''}`
												}">
												<div class="p-2 w-12 h-8 rounded-lg" :style="{backgroundColor: `var(--color-${type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })`}" />
												<div style="user-select: all;">
													<span><span class="font-bold">{{ type }}</span><span class="opacity-50 font-bold">-ui</span></span>
													<span>{{ role ? `-${role}` : '' }}</span>
													<span>{{ prominence ? `-${prominence}` : '' }}</span>
													<span>{{ interaction ? `-${interaction}` : '' }}</span>
												</div>
											</div>
											<div class="ui-docs-bg dark flex items-center gap-1"
												:style="{
													backgroundColor: `${role.startsWith('on') ? `var(--color-bg-ui-${role.replace('on', '')})` : ''}`,
													color: `${role.startsWith('on') ? `var(--color-${type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })` : ''}`
												}">
												<div class="p-2 w-12 h-8 rounded-lg" :style="{backgroundColor: `var(--color-${type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })`}" />
												<div style="user-select: all;">
													<span><span class="font-bold">{{ type }}</span><span class="opacity-50 font-bold">-ui</span></span>
													<span>{{ role ? `-${role}` : '' }}</span>
													<span>{{ prominence ? `-${prominence}` : '' }}</span>
													<span>{{ interaction ? `-${interaction}` : '' }}</span>
												</div>
											</div>
										</template>
									</div>
								</div>
							</div>
						</div>
					</details>
				</div>
			</div>
		</Variant>
	</Story>
</template>
<script setup lang="ts">
	const semantics = {
		type: ['bg', 'text', 'border', 'icon'],
		role: ['', 'brand', 'statistic', 'selected', 'danger', 'success', 'warning', 'neutral', 'disabled', 'onbrand', 'onstatistic', 'onselected', 'ondanger', 'onsuccess', 'onwarning', 'onneutral', 'ondisabled'],
		prominence: ['', 'secondary'], // maybe: strong, weak
		interaction: ['', 'hover'], // maybe: active
	}

	function shouldExist(type = '', role = '', prominence = '', interaction = '') {
		if ((type === 'bg' && role !== '' && role.startsWith('on')) || (type === 'bg' && prominence !== '') || (role.includes('disabled') && interaction !== '')) {
			return false;
		}
		return true;
	}

	// const filteredSemantics = semantics.type.flatMap((type) =>
	// 	semantics.role.flatMap((role) =>
	// 		semantics.prominence.flatMap((prominence) =>
	// 			semantics.interaction.filter((interaction) =>
	// 				shouldExist(type, role, prominence, interaction)
	// 			).map((interaction) => ({ type, role, prominence, interaction }))
	// 		)
	// 	)
	// );

	// const filteredSemanticsStrings = filteredSemantics.map(({ type, role, prominence, interaction }) => {
	// 	return `${type}-ui${role ? `-${role}` : ''}${prominence ? `-${prominence}` : ''}${interaction ? `-${interaction}` : ''}`;
	// });
</script>

<style lang="postcss" scoped>
.ui-docs-bg {
	@apply px-1 py-0.5;
	background-color: theme(colors.palette.neutral.100);
	color: theme(colors.palette.neutral.900);
}

.ui-docs-bg.dark {
	background-color: theme(colors.palette.neutral.800);
	color: theme(colors.palette.neutral.100);
}
</style>
